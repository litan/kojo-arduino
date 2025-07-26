// #include ~/kojo-includes/ka-bridge.kojo
switchToScriptEditingPerspective()
val echoPin = 10
val triggerPin = 9
val ledPin = 13
val pauseTime = 100

def setup() {
    pinMode(ledPin, OUTPUT)
    digitalWrite(ledPin, LOW)
    UltraSonic.init(triggerPin, echoPin)
}

def loop() {
    val microSeconds = UltraSonic.pingMicroSecs()
    val distanceToObstacle = .0343 * microSeconds / 2
    println(s"Distance to obstacle is - $distanceToObstacle cm")
    if (distanceToObstacle < 5) {
        digitalWrite(ledPin, HIGH)
        // move motors to turn right
        // do appropriate commands below
        // digitalWrite(pin, value)
        // etc
    }
    else {
        digitalWrite(ledPin, LOW)
        // keep going
        // do appropriate commands below
        // digitalWrite(pin, value)
        // etc
    }
    delay(pauseTime)
}