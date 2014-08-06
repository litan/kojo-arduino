// #include ../ka-bridge.kojo

val controlPin1 = 2 // h-bridge direction
val controlPin2 = 3 // h-bridge direction
val enablePin = 9 // h-bridge on/off
val directionSwitchPin = 4
val onOffSwitchPin = 5
val speedPin = 0

var onOffSwitchState = 0
var prevOnOffSwitchState = 0
var directionSwitchState = 0
var prevDirectionSwitchState = 0

var motorEnabled = false
var motorSpeed = 0
var motorDirection = false

def setup() {
    pinMode(directionSwitchPin, INPUT)
    pinMode(onOffSwitchPin, INPUT)
    pinMode(enablePin, OUTPUT)
    pinMode(controlPin1, OUTPUT)
    pinMode(controlPin2, OUTPUT)

    digitalWrite(enablePin, LOW)
}

def loop() {
    onOffSwitchState = digitalRead(onOffSwitchPin)
    directionSwitchState = digitalRead(directionSwitchPin)
    motorSpeed = analogRead(speedPin) / 4

    if (onOffSwitchState != prevOnOffSwitchState) {
        if (onOffSwitchState == HIGH) {
            motorEnabled = !motorEnabled
        }
        prevOnOffSwitchState = onOffSwitchState
    }

    if (directionSwitchState != prevDirectionSwitchState) {
        if (directionSwitchState == HIGH) {
            motorDirection = !motorDirection
        }
        prevDirectionSwitchState = directionSwitchState
    }

    if (motorDirection) {
        digitalWrite(controlPin1, HIGH)
        digitalWrite(controlPin2, LOW)
    }
    else {
        digitalWrite(controlPin1, LOW)
        digitalWrite(controlPin2, HIGH)
    }

    if (motorEnabled) {
        analogWrite(enablePin, motorSpeed)
    }
    else {
        analogWrite(enablePin, 0)
    }
}
