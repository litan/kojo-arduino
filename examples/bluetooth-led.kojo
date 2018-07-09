/**
 * (1) Set up a circuit with a bluetooth board connected to pins 10 and 11, and an LED on pin 12
 * Note - Kojo's SoftSerial uses pin 10 for RX and pin 11 for TX
 * (2) Run this program.
 * (3) Send 0,1 data to the bluetooth module using, for example, the Serial Bluetooth Terminal
 * app on your cell/mobile phone
 * (4) watch the LED in your circuit turn on and off!
 */
// #include ~/kojo-includes/ka-bridge.kojo
val ledPin = 12
val bluetoothModuleBaudRate = 38400
def setup() {
    pinMode(ledPin, OUTPUT)
    SoftSerial.begin(bluetoothModuleBaudRate)
}

def loop() {
    val av = SoftSerial.available
    if (av > 0) {
        println(s"Avalable: $av")
        repeatFor(1 to av) { idx =>
            val state = SoftSerial.read();
            if (state == '0') {
                digitalWrite(ledPin, LOW);
                println(s"$idx. LED: OFF")
                SoftSerial.println(s"$idx. LED: OFF"); 
            }
            else if (state == '1') {
                digitalWrite(ledPin, HIGH);
                println(s"$idx. LED: ON")
                SoftSerial.println(s"$idx. LED: ON");;
            }
            else {
                println(s"Unknow command: $state")
            }
            if (idx > 0) {
                delay(100)
            }
        }
    }
}