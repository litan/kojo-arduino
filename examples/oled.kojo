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
    Oled.setTextSize(1)
    Oled.println("Scrolling Hello")
}

def loop() {
    Oled.startScrollRight(0x00, 0x0F);
    delay(2000);
    Oled.stopScroll();
    delay(1000);
    Oled.startScrollLeft(0x00, 0x0F);
    delay(2000);
    Oled.stopScroll();
    delay(1000);
    Oled.startScrollDiagRight(0x00, 0x07);
    delay(2000);
    Oled.startScrollDiagLeft(0x00, 0x07);
    delay(2000);
    Oled.stopScroll();
    delay(1000);
}

