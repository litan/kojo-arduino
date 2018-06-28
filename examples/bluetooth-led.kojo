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
def setup() {
    pinMode(ledPin, OUTPUT)
    // 
    SoftSerial.begin(9600)
}

def loop() {
    val av = SoftSerial.available
    if (av > 0) {
        repeatFor(1 to av) { idx =>
            val state = SoftSerial.read(); 
            if (state == '0') {
                digitalWrite(ledPin, LOW); 
                println("LED: OFF")
                //     mySerial.println("LED: OFF"); 
            }
            else if (state == '1') {
                digitalWrite(ledPin, HIGH);
                println("LED: ON")
                //    mySerial.println("LED: ON");;
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