/*
Kojo Arduino Bridge -- Arduino side
 */
 
#include <Servo.h>
Servo servo;


#define IN_PACK_MAX_SIZE (10) // ns, proc, and eight more bytes for args
#define OUT_PACK_HDR_SIZE (3) // ret val type, ns, and proc
byte incoming_packet[IN_PACK_MAX_SIZE]; 
byte outgoing_packet_hdr[OUT_PACK_HDR_SIZE]; 
int counter;
int state;
int packetSize;

void setup() {
  Serial.begin(115200);
  counter = 0;
  state = 1;
}

void loop() {
}

void serialEvent() {
  while (Serial.available()) {
    switch (state) {
      case 1: // new packet
        packetSize = Serial.read();
        state = 2;
        break;
      case 2: // reading packet 
        byte input = Serial.read();
        incoming_packet[counter++] = input;
        if (counter == packetSize) {
          counter = 0;
          dispatchProc();
          counter = 0;
          packetSize = 0;
          state = 1;
        } 
        break;
    }
  }
}

int len(const char str[]) {
  int len = 0;
  char c = str[len] ;
  while (c != 0) {
    len++;
    c = str[len];
  }
  return len;
}

void log(String msg) {
  returnString(0, -1, msg);
}

byte readByte() {
  return incoming_packet[counter++];
}

unsigned int readInt() {
  byte lo = readByte();
  byte hi = readByte();
  unsigned int retVal = hi << 8 | lo;
//  String msg = String("Read Int: ") + retVal;
//  log(msg);
  return retVal;
}

void writeByte(byte b) {
  Serial.write(b);
}

void writeInt(unsigned int i) {
  Serial.write(i & 0x00FF);
  Serial.write(i >> 8);
}

void writeArray(byte arr[], int len) {
  Serial.write(arr, len);
}

void writeHeader(byte retType, byte ns, byte proc) {
  outgoing_packet_hdr[0] = retType;
  outgoing_packet_hdr[1] = ns;
  outgoing_packet_hdr[2] = proc;
  Serial.write(outgoing_packet_hdr, OUT_PACK_HDR_SIZE);
}

void returnByte(byte ns, byte proc, byte byteRet) {
  Serial.write(OUT_PACK_HDR_SIZE + 1); // packet size
  writeHeader(1, ns, proc);
  writeByte(byteRet);
}

void returnInt(byte ns, byte proc, unsigned int intRet) {
  Serial.write(OUT_PACK_HDR_SIZE + 2); // packet size
  writeHeader(2, ns, proc);
  writeInt(intRet);
}

void returnString(byte ns, byte proc, String msg) {
  int len = msg.length();
  Serial.write(OUT_PACK_HDR_SIZE + len);
  writeHeader(3, ns, proc);
  char buf[len+1];
  msg.toCharArray(buf, len+1);
  Serial.write(buf);
}

void dispatchProc() {
  int intRet;
  int writeSize;
  byte byteRet;
  byte ns = readByte();
  byte proc = readByte();
  switch (ns) {
    case 0: // meta
      switch (proc) {
        case 1: // kojo ping
          log(String("Board Ready"));
          returnInt(0, 1, 0xF0F0);
          break;
      }
      break;
    case 1: // inbuilt
      switch (proc) {
        case 1: // pinMode
          pinMode(readByte(), readByte());
          break;
        case 2: // digitalWrite
          digitalWrite(readByte(), readByte());
          break;
        case 3: // digitalRead
          returnByte(1, 3, digitalRead(readByte()));
          break;
        case 4: // analogRead
          returnInt(1, 4, analogRead(readByte()));
          break;
        case 5: // tone
          tone(readByte(), readInt());
          break;
        case 6: // noTone
          noTone(readByte());
          break;
        case 7: // analogWrite
          analogWrite(readByte(), readByte());
          break;
        case 8: // tone
          tone(readByte(), readInt(), readInt());
          break;
      }
      break;
    case 2: // servo lib
      switch (proc) {
        case 1: // attach
          servo.attach(readByte());
          break;
        case 2: // write
          servo.write(readByte());
          break;
      }
      break;
  }
}

