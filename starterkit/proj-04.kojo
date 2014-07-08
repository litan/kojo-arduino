// #include ../ka-bridge.kojo

val greenPin = 9
val bluePin = 10
val redPin = 11

def setup() {
    pinMode(greenPin, OUTPUT)
    pinMode(bluePin, OUTPUT)
    pinMode(redPin, OUTPUT)
}

def loop() {
    val redVal = analogRead(0)
    val greenVal = analogRead(1)
    val blueVal = analogRead(2)
    println(s"red: $redVal, green: $greenVal, blue: $blueVal")

    analogWrite(redPin, redVal/4)
    analogWrite(greenPin, greenVal/4)
    analogWrite(bluePin, blueVal/4)
    delay(5)
}
