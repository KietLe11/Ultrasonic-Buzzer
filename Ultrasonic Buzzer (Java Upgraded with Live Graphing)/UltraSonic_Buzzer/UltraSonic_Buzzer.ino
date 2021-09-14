

#define echoPin 6 // attach pin D2 Arduino to pin Echo of HC-SR04
#define trigPin 10 //attach pin D3 Arduino to pin Trig of HC-SR04
#define buzzer 3 //buzzer
#define LED 4 //led
// defines variables
long duration; // variable for the duration of sound wave travel
int distance; // variable for the distance measurement
int curr = 0;
int receivedData=0;

void setup() {
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an OUTPUT
  pinMode(echoPin, INPUT); // Sets the echoPin as an INPUT
  pinMode(buzzer, OUTPUT);
  pinMode(LED,OUTPUT);
  Serial.begin(9600); // // Serial Communication is starting with 9600 of baudrate speed
   // print some text in Serial Monitor
  
}


void sendDistanceData(){
  int value = distance;
  const byte data [] = {0,0,highByte(value),lowByte(value)};
  Serial.write(data, 4); // send 0,0, "high byte", "low byte"
  Serial.println(); // send "newline"
  
}

void loop() {

  if(distance<2000 && distance !=10 && distance != 0 && distance !=266){
    sendDistanceData();
    
  }
 
  receivedData = Serial.read();


  if(receivedData == 255){
    digitalWrite(LED, HIGH);
  }

  if(receivedData==254){
    digitalWrite(LED, LOW);
  }
  
  // Clears the trigPin condition
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  // Sets the trigPin HIGH (ACTIVE) for 10 microseconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(echoPin, HIGH);
  // Calculating the distance
  distance = duration * 0.034 / 2; // Speed of sound wave divided by 2 (go and back)
  // Displays the distance on the Serial Monitor

  if ((distance > curr+20) || (distance < curr-20)){
    digitalWrite(buzzer,HIGH);
    curr = distance;
    
  }
  else{
    digitalWrite(buzzer,LOW);
    curr = distance;
  }
  
}
