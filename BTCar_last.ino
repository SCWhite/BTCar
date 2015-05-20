#include <SoftwareSerial.h>
#include <Wire.h>//引用二個函式庫SoftwareSerial及Wire
#include <Servo.h>
#include <Ultrasonic.h>

#define TRIGGER_PIN1  38
#define ECHO_PIN1     39
#define TRIGGER_PIN2  34
#define ECHO_PIN2     35
#define TRIGGER_PIN3  36
#define ECHO_PIN3     37
#define dis           10
unsigned long startTime;                 // 按鈕被按下的起始時間
unsigned long duration;                  // 持續時間

SoftwareSerial I2CBT(10,11);//定義PIN10及PIN11分別為RX及TX腳位
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
const int led = 7;
Ultrasonic ultrasonic1(TRIGGER_PIN1, ECHO_PIN1);
Ultrasonic ultrasonic2(TRIGGER_PIN2, ECHO_PIN2);
Ultrasonic ultrasonic3(TRIGGER_PIN3, ECHO_PIN3);

void setup() {
   Serial.begin(9600); //Arduino起始鮑率：9600
   I2CBT.begin(9600); 
//藍牙鮑率：57600(注意！每個藍牙晶片的鮑率都不太一樣，請務必確認
   pinMode(13, OUTPUT);  //設定 pin13 為輸出，LED就接在這
  //伺服馬達
  servoX.attach(2,500, 2400); // 設定伺服馬達的接腳
  servoY.attach(12,500, 2400);
  servoX.write(90); // 一開始先置中90度 180
  servoY.write(90); // 一開始先置中90度   0
  //直流馬達
  pinMode(motorIn1, OUTPUT);
  pinMode(motorIn2, OUTPUT);
  pinMode(motorIn3, OUTPUT);
  pinMode(motorIn4, OUTPUT);
  pinMode(44, OUTPUT);
  pinMode(45, OUTPUT);
  //pinMode(8, OUTPUT);
  pinMode(7, OUTPUT);
}

void loop() {
  byte cmmd[20];
  int insize;
  //analogWrite(9, 150);//速度設定
  digitalWrite(led,LOW);
  //預設刷頭 關
  while(1){
    startTime = millis();// 記錄按鈕被按下的起始時間
    sonic();
    duration = millis() - startTime;// 計算按鈕被按了多久
    Serial.print("duration: ");              // 印出 duration 的內容
    Serial.print(duration);
    Serial.println(" microseconds");
    
    analogWrite(9, 150);//速度設定
    analogWrite(8, 150);//速度設定
    if ((insize=(I2CBT.available()))>0){  //讀取藍牙訊息
    sonic();
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
        case 101://101為"e"的ASCII CODE
           //Serial.println("Get b");
           digitalWrite(7,HIGH);   //點亮照明LED
           break;
        case 100://100為"d"的ASCII CODE
           //Serial.println("Get b");
           digitalWrite(7,LOW);   //熄滅照明LED
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
           /*posX=posX+20;
           if(posX>=2400){
              posX=2400;
           }
           servoX.writeMicroseconds(posX);*/
           servoX.write(75);
           servoY.write(105);
           break;
       case 120://120為"x"的ASCII CODE
           /*posX=posX-20;
           if(posX<=500){
             posX=500;
           }
           servoX.writeMicroseconds(posX);*/
           servoX.write(140);
           servoY.write(50);
           break;
       /*case 121://121為"y"的ASCII CODE
           /*posY=posY+20;
           if(posY>=2400){
              posY=2400;
           }
           servoY.writeMicroseconds(posY);
           servoX.write(180);
           servoY.write(0);
           break;
       case 122://122為"z"的ASCII CODE
           /*posY=posY-20;
           if(posY<=500){
             posY=500;
           }
           servoY.writeMicroseconds(posY);
           servoX.write(0);
           servoY.write(180);
           break;
           */
        case 111://111為"o"的ASCII CODE
           //turn on
           digitalWrite(44,HIGH);
           digitalWrite(45,HIGH);
           break;
        case 99://9為"c"的ASCII CODE
           //turn off
           digitalWrite(44,LOW);
           digitalWrite(45,LOW);
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
void sonic(){
  //int i=0;
  float cmMsec1, inMsec1;
  float cmMsec2, inMsec2;
  float cmMsec3, inMsec3;
  long microsec1 = ultrasonic1.timing();
  long microsec2 = ultrasonic2.timing();
  long microsec3 = ultrasonic3.timing();

  cmMsec1 = ultrasonic1.convert(microsec1, Ultrasonic::CM); 
  cmMsec2 = ultrasonic2.convert(microsec2, Ultrasonic::CM); 
  cmMsec3 = ultrasonic3.convert(microsec3, Ultrasonic::CM);
  /*
  Serial.print("First MS: ");
  Serial.print(microsec1);
  Serial.print(", CM: ");
  Serial.print(cmMsec1);
  Serial.println( );
  
  Serial.print("Second MS: ");
  Serial.print(microsec2);
  Serial.print(", CM: ");
  Serial.print(cmMsec2);
  Serial.println( );
  
  Serial.print("Third MS: ");
  Serial.print(microsec3);
  Serial.print(", CM: ");
  Serial.print(cmMsec3);
  Serial.println( );
  */
  if(cmMsec1<=dis&&cmMsec1!=0){//左邊超音波
    backward();
    delay(300);
    stops();
    right();
    //delay(300);
    stops();
  }
  if(cmMsec3<=dis&&cmMsec3!=0){//右邊超音波
    backward();
    delay(300);
    stops();
    left();
    //delay(300);
    stops();
  }
  if(cmMsec2<=dis&&cmMsec2!=0){//後面超音波
    forward();
    delay(300);
    stops();
  }
}
