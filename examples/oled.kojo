// #include ~/kojo-includes/ka-bridge.kojo

switchToScriptEditingPerspective()
val ledPin = 13
val pauseTime = 3000

var x = 1

def setup() {
    pinMode(ledPin, OUTPUT)
    digitalWrite(ledPin, LOW)
    Oled.init(0x3C)
    Oled.clearDisplay()
    Oled.println("Hello")
    Oled.startScrollLeft(0, 7)
    delay(5000)
    Oled.stopScroll()
}

def loop() {
}

