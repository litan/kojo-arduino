// #include ~/kojo-includes/ka-bridge.kojo

switchToScriptEditingPerspective()
val ledPin = 13
val pauseTime = 1000

def setup() {
    pinMode(ledPin, OUTPUT)
}

def loop() {
    digitalWrite(ledPin, HIGH)
    delay(pauseTime)
    digitalWrite(ledPin, LOW)
    delay(pauseTime)
}