/**
 * (1) Set up a circuit with a light sensor connected to analog pin 0, and an LED on pin 12.
 * (2) Play with the light falling on the light sensor -- by running the program in a dark room 
 *     and switching on and off the light in the room, for example.
 * (3) Look at the lightLevel values getting printed in the output pane, and determine an appropriate
 *     darkness threshold. Stop the program and set 'val darknessThreashold' in the program to this value.
 * (4) Run the program again and watch the LED turn on and off as you switch the light in your room off and on!
 */

// #include ~/kojo-includes/ka-bridge.kojo
switchToScriptEditingPerspective()
val ledPin = 12
val darknessThreashold = 400
def setup() {
    pinMode(ledPin, OUTPUT)
}

def loop() {
    val lightLevel = analogRead(0)
    println(lightLevel)
    if (lightLevel < darknessThreashold) {
        digitalWrite(ledPin, HIGH)
    }
    else {
        digitalWrite(ledPin, LOW)
    }
    delay(100)
}