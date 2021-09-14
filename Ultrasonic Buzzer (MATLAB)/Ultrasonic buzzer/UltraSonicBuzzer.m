%YouTube Video Link: 
%https://youtu.be/amZQnNcVPbk
a = arduino('COM8', 'uno','Libraries', 'Ultrasonic');

ultrasonicObj = ultrasonic(a,'D10','D7','OutputFormat','double');
curr=0;
button = 0;

while button~=1
   clear distance
   distance = readDistance(ultrasonicObj);
    
   if ((distance > curr+0.1) || (distance < curr-0.1))
   writeDigitalPin(a,'D5',1)
   writeDigitalPin(a,'D4',1)
   curr = distance;
   else
   writeDigitalPin(a,'D5',0)
   writeDigitalPin(a,'D4',0)
   
   end
   button = readDigitalPin(a,'D6');
end
clear all