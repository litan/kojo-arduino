// #include ../ka-bridge.kojo
val switchpin = 8
var pretime = 0l
var switchstate = 0
var preswitchstate = 0
var led = 2
val interval = 3000
def setup() {
    repeatFor(2 until 8) { x =>
        pinMode(x, OUTPUT)
    }
    pinMode(switchpin, INPUT)
}
def loop() {
    var currenttime = millis
    if (currenttime - pretime > interval) {
        pretime = currenttime
        digitalWrite(led, HIGH)
        led = led + 1
        val duration = 500
        if (led == 8) {
            repeat(4) {
                tone(12, 262, duration)
                delay(duration)
                tone(12, 330, duration)
                delay(duration)
            }
        }
//        clearOutput()
    }
    switchstate = digitalRead(switchpin)
    if (switchstate != preswitchstate) {
        repeatFor(2 until 8) { x =>
            digitalWrite(x, LOW)
        }
        led = 2
        pretime = currenttime
    }
    preswitchstate = switchstate
    delay(5)
}