#include <SoftwareSerial.h>
#include <Wire.h>//引用二個函式庫SoftwareSerial及Wire

SoftwareSerial I2CBT(10,11);//定義PIN10及PIN11分別為RX及TX腳位

const int motorIn1 = 3;
const int motorIn2 = 4;
const int motorIn3 = 5;
const int motorIn4 = 6;

void setup() {
   Serial.begin(9600); //Arduino起始鮑率：9600
   I2CBT.begin(9600); 
//藍牙鮑率：57600(注意！每個藍牙晶片的鮑率都不太一樣，請務必確認
   pinMode(13, OUTPUT);  //設定 pin13 為輸出，LED就接在這
}

void loop() {
byte cmmd[20];
int insize;	
while(1){
   if ((insize=(I2CBT.available()))>0){  //讀取藍牙訊息
      Serial.print("input size = ");
      Serial.println(insize);
      for (int i=0; i<insize; i++){
Serial.print(cmmd[i]=char(I2CBT.read()));
Serial.print(" ");
      }//此段請參考上一篇解釋
   }
   switch (cmmd[0]) { //讀取第一個字
      case 97: //97為"a"的ASCII CODE
         digitalWrite(13,HIGH);  //點亮LED
         break;

      case 98://98為"b"的ASCII CODE
         Serial.println("Get b");
         digitalWrite(13,LOW);   //熄滅LED
         break;
     case 102://102為"f"的ASCII CODE
         forward();
         break;
     case 115://115為"s"的ASCII CODE
         stops();
         break;
     case 107://107為"k"的ASCII CODE b:98
         backward();
         break;
     case 114://114為"r"的ASCII CODE
         right();
         break;
     case 108://108為"l"的ASCII CODE
         left();
         break;
   } //Switch
   } //while
}//loop

void forward()
{
  digitalWrite(motorIn1, HIGH);
  digitalWrite(motorIn2, LOW);
  digitalWrite(motorIn3, HIGH);
  digitalWrite(motorIn4, LOW);
}

void backward()
{
  digitalWrite(motorIn1, LOW);
  digitalWrite(motorIn2, HIGH);
  digitalWrite(motorIn3, LOW);
  digitalWrite(motorIn4, HIGH);
}

// Let right motor keep running, but stop left motor
void right()
{
  digitalWrite(motorIn1, HIGH);
  digitalWrite(motorIn2, LOW);
  digitalWrite(motorIn3, LOW);
  digitalWrite(motorIn4, LOW);
}

// Let left motor keep running, but stop right motor
void left()
{
  digitalWrite(motorIn1, LOW);
  digitalWrite(motorIn2, LOW);
  digitalWrite(motorIn3, HIGH);
  digitalWrite(motorIn4, LOW);
}
void stops()
{
  digitalWrite(motorIn1, LOW);
  digitalWrite(motorIn2, LOW);
  digitalWrite(motorIn3, LOW);
  digitalWrite(motorIn4, LOW);
}

