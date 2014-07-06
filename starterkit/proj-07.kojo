// #include ../ka-bridge.kojo
val buttonSensor = 0
val piezoPin = 8

def setup() {
}

val notes = Seq(262, 294, 330, 349)

def loop() {
    val x = analogRead(buttonSensor)
    println(s"Sensor Reading: $x")

    if (x < 2) {
        noTone(piezoPin)
    }
    else if (x < 200) {
        tone(piezoPin, notes(0))
    }
    else if (x < 600) {
        tone(piezoPin, notes(1))
    }
    else if (x < 1010) {
        tone(piezoPin, notes(2))
    }
    else {
        tone(piezoPin, notes(3))
    }
}
