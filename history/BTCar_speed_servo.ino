#include <SoftwareSerial.h>
//#include <Wire.h>//引用二個函式庫SoftwareSerial及Wire
#include <Servo.h>

//定義PIN10及PIN11分別為RX及TX腳位
SoftwareSerial I2CBT(10,11);

//伺服馬達
Servo servoX, servoY;
int valX, posX=1450; // 暫存類比輸入值的變數
int valY, posY=1450;

//直流馬達
const int motorIn1 = 4;
const int motorIn2 = 3;
const int motorIn3 = 5;
const int motorIn4 = 6;
//const int ena = 9;
//int s=150;

void setup() {
  //Arduino起始鮑率：9600
  Serial.begin(9600); 
  I2CBT.begin(9600); 
  pinMode(13, OUTPUT);  //設定 pin13 LED就接在這
  
  //伺服馬達
  //servoX.attach(7,500, 2400); // 設定伺服馬達的接腳
  //servoY.attach(8,500, 2400);
  servoX.write(90); // 一開始先置中90度
  servoY.write(90); // 一開始先置中90度
  
  //直流馬達
  pinMode(motorIn1, OUTPUT);
  pinMode(motorIn2, OUTPUT);
  pinMode(motorIn3, OUTPUT);
  pinMode(motorIn4, OUTPUT);
}

void loop() {
  byte cmmd[20];
  int insize;
  
  while(1){
    analogWrite(9, 150);//速度設定
    analogWrite(8, 150);//速度設定
    if ((insize=(I2CBT.available()))>0){  //讀取藍牙訊息
      Serial.print("input size = ");
      Serial.println(insize);
      for (int i=0; i<insize; i++){
        Serial.print(cmmd[i]=char(I2CBT.read()));
        Serial.print(" ");
      }
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
       case 119://119為"w"的ASCII CODE
           posX=posX+20;
           if(posX>=2400){
              posX=2400;
           }
           servoX.writeMicroseconds(posX);
           break;
       case 120://120為"x"的ASCII CODE
           posX=posX-20;
           if(posX<=500){
             posX=500;
           }
           servoX.writeMicroseconds(posX);
           break;
       case 121://121為"y"的ASCII CODE
           posY=posY+20;
           if(posY>=2400){
              posY=2400;
           }
           servoY.writeMicroseconds(posY);
           break;
       case 122://122為"z"的ASCII CODE
           posY=posY-20;
           if(posY<=500){
             posY=500;
           }
           servoY.writeMicroseconds(posY);
           break;
    } //Switch
  } //while
}//loop

void forward()
{
  analogWrite(9, 150);
  analogWrite(8, 150);
  digitalWrite(motorIn1, HIGH);
  digitalWrite(motorIn2, LOW);
  digitalWrite(motorIn3, HIGH);
  digitalWrite(motorIn4, LOW);
}

void backward()
{
  analogWrite(9, 150);
  analogWrite(8, 150);
  digitalWrite(motorIn1, LOW);
  digitalWrite(motorIn2, HIGH);
  digitalWrite(motorIn3, LOW);
  digitalWrite(motorIn4, HIGH);
}

// Let right motor keep running, but stop left motor
void right()
{
  analogWrite(9, 120);
  digitalWrite(motorIn1, HIGH);
  digitalWrite(motorIn2, LOW);
  digitalWrite(motorIn3, LOW);
  digitalWrite(motorIn4, LOW);
}

// Let left motor keep running, but stop right motor
void left()
{
  analogWrite(9, 120);
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
