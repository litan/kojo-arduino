// #include ../ka-bridge.kojo

var sensorLo = 1023
var sensorHi = 0

def calibrate() {
    val t0 = epochTime
    while (epochTime - t0 < 5) {
        val lightLevel = analogRead(0)
        if (lightLevel < sensorLo) {
            sensorLo = lightLevel
        }
        else if (lightLevel > sensorHi) {
            sensorHi = lightLevel
        }
    }
    println(s"Sensor range: $sensorLo - $sensorHi")
}

def setup() {
    calibrate()
}

def loop() {
    val lightLevel = analogRead(0)
    val freq = kmath.map(lightLevel, sensorLo, sensorHi, 50, 4000).toInt
    tone(8, freq, 20)
    delay(20)
}
