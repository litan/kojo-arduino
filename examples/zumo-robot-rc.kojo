// #include ~/kojo-includes/ka-bridge.kojo

// for use with a Zumo Robot for Arduino
// https://www.pololu.com/product/2510

// Hardware setup (brief) instructions:
// Mount an arduino on the zumo robot
// Put 4 AA batteries in the robot
// Turn on the robot
// Connect the arduino to your computer/rpi via a serial cable
// And you are good to go with this remote-control script ...

def setup() {
    ZumoMotors.init()
}

cleari()
val rightTurnBtn = fillColor(black) -> Picture.rectangle(50, 50)
val leftTurnBtn = fillColor(black) -> Picture.rectangle(50, 50)
val forwardBtn = fillColor(black) -> Picture.rectangle(100, 50)
val spotTurnBtn = fillColor(black) -> Picture.rectangle(100, 50)
val reverseBtn = fillColor(black) -> Picture.rectangle(100, 50)
val pic = picCol(reverseBtn, spotTurnBtn, forwardBtn, picRow(leftTurnBtn, rightTurnBtn))
draw(pic)

@volatile var leftTurn = false
@volatile var rightTurn = false
@volatile var spotTurn = false
@volatile var forwardGo = false
@volatile var reverseGo = false

rightTurnBtn.onMousePress { (x, y) =>
    rightTurn = true
    rightTurnBtn.setFillColor(red)
}

rightTurnBtn.onMouseRelease { (x, y) =>
    rightTurn = false
    rightTurnBtn.setFillColor(black)
}

leftTurnBtn.onMousePress { (x, y) =>
    leftTurn = true
    leftTurnBtn.setFillColor(red)
}

leftTurnBtn.onMouseRelease { (x, y) =>
    leftTurn = false
    leftTurnBtn.setFillColor(black)
}

spotTurnBtn.onMousePress { (x, y) =>
    spotTurn = true
    spotTurnBtn.setFillColor(red)
}
spotTurnBtn.onMouseRelease { (x, y) =>
    spotTurn = false
    spotTurnBtn.setFillColor(black)
}

forwardBtn.onMousePress { (x, y) =>
    forwardGo = true
    forwardBtn.setFillColor(red)
}
forwardBtn.onMouseRelease { (x, y) =>
    forwardGo = false
    forwardBtn.setFillColor(black)
}

reverseBtn.onMousePress { (x, y) =>
    reverseGo = true
    reverseBtn.setFillColor(red)
}
reverseBtn.onMouseRelease { (x, y) =>
    reverseGo = false
    reverseBtn.setFillColor(black)
}

@volatile var leftMotorRunning = false
@volatile var leftMotorForward = false
@volatile var rightMotorRunning = false
@volatile var rightMotorForward = false

val speed = 100

def driveLeftMotor() {
    if (!leftMotorRunning) {
        leftMotorRunning = true
        leftMotorForward = true
        ZumoMotors.setLeftSpeed(speed)
    }
}

def reverseLeftMotor() {
    if (!leftMotorRunning) {
        leftMotorRunning = true
        leftMotorForward = false
        ZumoMotors.setLeftSpeed(-speed)
    }
}

def stopLeftMotor() {
    if (leftMotorRunning) {
        leftMotorRunning = false
        ZumoMotors.setLeftSpeed(0)
    }
}

def driveRightMotor() {
    if (!rightMotorRunning) {
        rightMotorRunning = true
        rightMotorForward = true
        ZumoMotors.setRightSpeed(speed)
    }
}

def reverseRightMotor() {
    if (!rightMotorRunning) {
        rightMotorRunning = true
        rightMotorForward = false
        ZumoMotors.setRightSpeed(-speed)
    }
}

def stopRightMotor() {
    if (rightMotorRunning) {
        rightMotorRunning = false
        ZumoMotors.setRightSpeed(0)
    }
}

def loop() {
    if (spotTurn) {
        driveLeftMotor()
        reverseRightMotor()
    }
    else if (forwardGo) {
        driveLeftMotor()
        driveRightMotor()
    }
    else if (reverseGo) {
        reverseLeftMotor()
        reverseRightMotor()
    }
    else if (rightTurn) {
        driveLeftMotor()
    }
    else if (leftTurn) {
        driveRightMotor()
    }
    else {
        stopLeftMotor()
        stopRightMotor()
    }
    delay(100)
}
