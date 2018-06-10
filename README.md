 To program an Arduino board with Kojo, you need to set up a bridge between Kojo (running on your computer) and the Arduino board. This bridge has two components:
* A program called ka_bridge.ino that runs on the Arduino board.
* A file called ka-bridge.kojo that you can include within your Kojo programs.
These two components talk to each other to do whatever Arduino specific things you want to do in your Kojo based Arduino programs.

To set up the Kojo-Arduino bridge, you need to do the following:

* Upload [ka_bridge.ino](https://github.com/litan/kojo-arduino/blob/master/ka_bridge/ka_bridge.ino) to your Arduino board using the [Arduino IDE](http://arduino.cc/en/Guide/Environment).
* Save [ka-bridge.kojo](https://github.com/litan/kojo-arduino/blob/master/ka-bridge.kojo) to a directory (let's say ~/kojo-includes) on your machine, so that you can include it within your Kojo based Arduino programs.

Once the Kojo-Arduino bridge is set up, you can start writing [Kojo based Arduino programs](https://github.com/litan/kojo-arduino/blob/master/starterkit/proj-02.kojo).

Some examples (ports of the examples in the [official Arduino Starter Kit](http://arduino.cc/en/Main/ArduinoStarterKit)) are available to help you get going:

[Starter Kit examples](https://bitbucket.org/lalit_pant/kojo-arduino/src/tip/starterkit/)

