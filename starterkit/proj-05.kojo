// #include ../ka-bridge.kojo

def setup() {
    Servo.attach(9)
}

def loop() {
    val turn = analogRead(0)
    val motorAngle = kmath.map(turn, 0, 1023, 0, 179).toInt
    println(s"Pot value: $turn; motor angle: $motorAngle")
    Servo.write(motorAngle)
    delay(5)
}
