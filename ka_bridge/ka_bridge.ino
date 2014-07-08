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
          dispatchCommand();
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
  int len = msg.length();
  Serial.write(OUT_PACK_HDR_SIZE + len);
  outgoing_packet_hdr[0] = 3; // return value type
  outgoing_packet_hdr[1] = 1; // ns
  outgoing_packet_hdr[2] = -1; // cmd
  writeArray(outgoing_packet_hdr, OUT_PACK_HDR_SIZE);
  char buf[len+1];
  msg.toCharArray(buf, len+1);
  Serial.write(buf);
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

void writeInt(int i) {
  Serial.write(i & 0x00FF);
  Serial.write(i >> 8);
}

void writeArray(byte arr[], int len) {
  Serial.write(arr, len);
}

void dispatchCommand() {
  int intRet;
  int writeSize;
  byte byteRet;
  byte ns = readByte();
  byte proc = readByte();
  switch (ns) {
    case 1: // inbuilt
      switch (proc) {
        case 1: // pinMode
          pinMode(readByte(), readByte());
          break;
        case 2: // digitalWrite
          digitalWrite(readByte(), readByte());
          break;
        case 3: // digitalRead
          writeSize = OUT_PACK_HDR_SIZE + 1;
          Serial.write(writeSize); // packet size
          outgoing_packet_hdr[0] = 1; // return value type
          outgoing_packet_hdr[1] = 1; // ns
          outgoing_packet_hdr[2] = 3; // proc
          byteRet = digitalRead(readByte()); // return value
          writeArray(outgoing_packet_hdr, OUT_PACK_HDR_SIZE);
          writeByte(byteRet);
          break;
        case 4: // analogRead
          writeSize = OUT_PACK_HDR_SIZE + 2;
          Serial.write(writeSize); // packet size
          outgoing_packet_hdr[0] = 2; // return value type
          outgoing_packet_hdr[1] = 1; // ns
          outgoing_packet_hdr[2] = 4; // proc
          intRet = analogRead(readByte()); // return value
          Serial.write(outgoing_packet_hdr, OUT_PACK_HDR_SIZE);
          writeInt(intRet);
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

