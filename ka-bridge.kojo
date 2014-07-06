import jssc.SerialPortList
import jssc.SerialPort
import jssc.SerialPortEventListener
import jssc.SerialPortEvent
import concurrent.Promise
import concurrent.Future
import concurrent.Await
import concurrent.duration._
import java.nio.ByteBuffer

clearOutput()
val names = SerialPortList.getPortNames
println(s"Ports: ${names.toList}")
val portName = names(0)
val serialPort = new SerialPort(portName)
println(s"Opening port: $portName")
serialPort.openPort()
serialPort.setParams(SerialPort.BAUDRATE_115200,
    SerialPort.DATABITS_8,
    SerialPort.STOPBITS_1,
    SerialPort.PARITY_NONE,
    true,
    true)

serialPort.addEventListener(new SerialPortReader())

def writeArray(arr: Array[Byte]) {
    serialPort.writeBytes(arr)
}

def writeInt(i: Int) {
    serialPort.writeInt(i & 0x00FF)
    serialPort.writeInt(i >> 8)
}

def pinMode(pin: Byte, mode: Byte) {
    // INPUT - 0; OUTPUT - 1
    val command = Array[Byte](4, 1, 1, pin, mode)
    //                        sz,ns,cmd,arg1,arg2
    writeArray(command)
}

def digitalWrite(pin: Byte, value: Byte) {
    // LOW - 0; HIGH - 1
    val command = Array[Byte](4, 1, 2, pin, value)
    //                        sz,ns,cmd,arg1,arg2
    writeArray(command)
}

@volatile var bytePromise: Promise[Byte] = _
@volatile var intPromise: Promise[Int] = _

def awaitResult[T](f: Future[T]): T = {
    try {
        Await.result(f, 1.seconds)
    }
    catch {
        case e: Throwable =>
            if (!done) throw e
            else throw new RuntimeException("None; Program Stopped.")
    }
}

def digitalRead(pin: Byte): Byte = {
    val command = Array[Byte](3, 1, 3, pin)
    //                        sz,ns,cmd,arg1
    bytePromise = Promise()
    writeArray(command)
    awaitResult(bytePromise.future)
}

def analogRead(pin: Byte): Int = {
    val command = Array[Byte](3, 1, 4, pin)
    //                        sz,ns,cmd, arg1
    intPromise = Promise()
    writeArray(command)
    awaitResult(intPromise.future)
}

def tone(pin: Byte, freq: Int) {
    writeArray(Array[Byte](5, 1, 5, pin))
    writeInt(freq)
}

def noTone(pin: Byte) {
    writeArray(Array[Byte](3, 1, 6, pin))
}

object Servo {
    // proxy for servo library
    // namespace (ns) = 2
    def attach(pin: Byte) {
        val command = Array[Byte](3, 2, 1, pin)
        //                        sz,ns,cmd,arg1
        writeArray(command)
    }

    def write(angle: Byte) {
        val command = Array[Byte](3, 2, 2, angle)
        //                        sz,ns,cmd,arg1
        writeArray(command)
    }
}

val INPUT, LOW = 0.toByte
val OUTPUT, HIGH = 1.toByte

def delay(n: Int) = Thread.sleep(n)

@volatile var done = false

runInBackground {
    pause(1)
    readln("Enter to write")
    runInBackground {
        readln("Enter to close serial port")
        done = true
        serialPort.closePort()
    }
    setup()
    while (!done) {
        loop()
    }
}

val debug = false
def debugMsg(msg: String) {
    if (debug) {
        println(msg)
    }
}

class SerialPortReader extends SerialPortEventListener {
    //    var currPacket: ByteBuffer = _
    var currData = ByteBuffer.allocate(0)
    var state = 1 // new packet
    var packetSize = 0
    var bytesAvailable = 0

    def readByte: Byte = {
        currData.get
    }

    def readInt: Int = {
        val lowByte: Int = readByte & 0x00FF
        val hiByte: Int = readByte & 0x00FF
        hiByte << 8 | lowByte
    }

    def readString: String = {
        val buf = new Array[Byte](packetSize - 3)
        currData.get(buf)
        new String(buf)
    }

    def serialEvent(event: SerialPortEvent) = synchronized {
        if (event.isRXCHAR && event.getEventValue > 0) { //If data is available
            val data = serialPort.readBytes(event.getEventValue)
            debugMsg(s"Arduino -> ${data.toList}")
            if (currData.hasRemaining) {
                val combinedData = ByteBuffer.allocate(currData.remaining + data.length)
                combinedData.put(currData)
                combinedData.put(data)
                currData = combinedData
                currData.flip()
            }
            else {
                currData = ByteBuffer.wrap(data)
            }
            handleData0()
        }
    }

    def handleData0() {
        state match {
            case 1 =>
                packetSize = currData.get
                bytesAvailable = currData.limit - currData.position
                state = 2
                handleData()
            case 2 =>
                bytesAvailable = currData.limit - currData.position
                handleData()
        }
    }

    def handleData() {
        debugMsg(s"Bytes available: $bytesAvailable, Curr packet size: $packetSize")
        if (bytesAvailable >= packetSize) {
            readByte match {
                case 1 => // byte
                    readByte; readByte
                    bytePromise.success(readByte)
                case 2 => // int
                    readByte; readByte
                    intPromise.success(readInt)
                case 3 => // string
                    readByte; readByte
                    val msg = readString
                    println(s"[Ard-Log] $msg")
            }
            packetDone()
        }
    }

    def packetDone() {
        state = 1
        if (currData.hasRemaining) {
            handleData0()
        }
    }
}

import language.implicitConversions
implicit def i2b(i: Int) = i.toByte
