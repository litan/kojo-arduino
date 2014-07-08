// #include ../ka-bridge.kojo

def setup() {
    repeatFor(2 to 4) { e =>
        pinMode(e, OUTPUT)
    }
}

def loop() {
    val reading = analogRead(0)
    val voltage = kmath.map(reading, 0, 1023, 0, 5)
    val temp = (voltage - 0.5) * 100
    println(temp)

    if (temp < 29) {
        repeatFor(2 to 4) { e =>
            digitalWrite(e, LOW)
        }
    }
    else if (temp < 31) {
        digitalWrite(2, HIGH)
        digitalWrite(3, LOW)
        digitalWrite(4, LOW)
    }
    else if (temp < 33) {
        digitalWrite(2, HIGH)
        digitalWrite(3, HIGH)
        digitalWrite(4, LOW)
    }
    else {
        digitalWrite(2, HIGH)
        digitalWrite(3, HIGH)
        digitalWrite(4, HIGH)
    }
    delay(5)
}

