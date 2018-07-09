 To program an Arduino board with Kojo, you need to set up a bridge between Kojo (running on your computer or on a [Raspberry Pi](http://www.kogics.net/kojo-download#rpi)) and the Arduino board. This bridge has two components:
* A program called ka_bridge.ino that runs on the Arduino board. This program listens for requests from Kojo and carries them out.
* A file called ka-bridge.kojo that you can include within your Kojo programs. This file contains commands/functions that you can use in your Kojo programs to communicate with ka_bridge.ino running on the Arduino board (to, for example, read sensors and control motors/actuators).

To set up the Kojo-Arduino bridge, you need to do the following:

* Upload [ka_bridge.ino](https://github.com/litan/kojo-arduino/blob/master/ka_bridge/ka_bridge.ino) to your Arduino board using the [Arduino IDE](http://arduino.cc/en/Guide/Environment).
* Save [ka-bridge.kojo](https://github.com/litan/kojo-arduino/blob/master/ka-bridge.kojo) to a directory (let's say ~/kojo-includes) on your machine, so that you can include it within your Kojo programs.

Once the Kojo-Arduino bridge is set up, you can start writing [Kojo based Arduino programs](https://github.com/litan/kojo-arduino/blob/master/examples/darkness-led.kojo).

A bunch of examples are available to help you get going:
* [Examples folder](https://github.com/litan/kojo-arduino/tree/master/examples) in this repo.
* [Ports of the examples](https://github.com/litan/kojo-arduino/tree/master/starterkit) in the [official Arduino Starter Kit](http://arduino.cc/en/Main/ArduinoStarterKit).

