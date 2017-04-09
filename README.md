#gameboy zero w sonos controler

## Interface
![alt text](http://i67.tinypic.com/28bbcra.png "Logo Title Text 1")


## setup

### running the jar
```sh
to run the jar
sonos ip = ip addres
clientid = spotify client id
clientsecretkey = spotify client secret key
userid = username
fullscreen mode = true ore false
wifiManger (turns wifi on and off in sleep mode) = true ore false

java -jar gameboy-sonos-remotes sonosip clientid clientsecretKey userid wifiManger
```
### setup off 3.5 inc tft screen

```sh
screen setup

cd /boot
sudo nano config.txt

see piSettingsFiles for config.txt changes
link
https://github.com/simondid/gameboy-sonos-remotes/blob/master/readMeImages/config.txt
```

### runnable .sh file no terminal window output

```sh
#!/bin/bash

sudo nohup java -jar "path to youre jar "/gameboy-sonos-remotes.jar sonosip clientid clientsecretKey userid fullscreenMode

```
run in therminal on the script file
```sh
sudo chmod +x "youre .sh filename"
```
### runnable .sh file with terminal window

```sh
#!/bin/bash

sudo java -jar "path to youre jar "/gameboy-sonos-remotes.jar sonosip clientid clientsecretKey userid fullscreenMode

```
run in therminal on the script file
```sh
sudo chmod +x "youre .sh filename"
```
### auto .sh file on boot (dos the job but bad fix)
auto start lxterminal and the lxterminal auto starts a .sh script

```sh
sudo nano .config/lxsession/LXDE-pi/autostart
```

add to autostart file

```sh
@lxterminal
```
fixing lxterminal window size
```sh
@lxterminal --geometry=39x9
```

then edit
```sh
sudo nano .bashrc
```
add to the end off the file no < >

```sh
bash < my .sh file name if located in /home/pi/ >
```

### setting raspbian ssh password

```sh
passwd
```
follow instructions

### ACT and PWR LED controle
commands work but seems unstable multiple class seems to be the bedst fix atm

```
echo 0 | sudo tee /sys/class/leds/led1/brightness

echo 1 | sudo tee /sys/class/leds/led0/brightness
```




# hardware
pi zero w
adafruit powerboost 1000c
lipo fuel gauge max 17043
2000mha lipo battery ( power test screen off for the intire test gives 7 houres and 1 minutes off battery life )


## electronics wiring

### screen on / off controler
new cercuit

using a fqp47p06

http://www.mouser.com/ds/2/149/FQP47P06-1009447.pdf
http://i63.tinypic.com/21e9aw2.jpg


![screen on / off controler](http://i63.tinypic.com/21e9aw2.jpg "screen on / off controler")

```
old screen controler circuit

http://i63.tinypic.com/11jc6mq.png
http://electronics.stackexchange.com/questions/233205/npn-transistor-to-run-12v-0-5a-from-3-3v-4ma

gpio 1 aka pin 12 on the raspberry pi model b

![screen on / off controler](http://i63.tinypic.com/11jc6mq.png "screen on / off controler")
```
### battery management circuit based on the max17043 lipo fuel gauge
http://i65.tinypic.com/xdf0wi.png
https://github.com/simondid/MAX17043

the battery management code is gonna be based on my max17043 class

https://github.com/simondid/MAX17043


![battery circuit](http://i65.tinypic.com/xdf0wi.png "battery circuit")


# optional

## power saving

### cpu throttling / dynamic cpu speed

powersvaings uncorfirmed

in /boot/config.txt

force_turbo=0
arm_freq_min=200

^makes the cpu switch between default cpy speed and the value off arm_freq_min

http://with-raspberrypi.blogspot.dk/2014/03/cpu-frequency.html

### wifi on and off

power svaings messured with low accuracy but around 3 ma

this is included in the program and is enabled bay default

see post bay "yavuzaksan"

wifi off
sudo iwconfig wlan0 txpower off

wifi on
sudo iwconfig wlan0 txpower 1

http://raspberrypi.stackexchange.com/questions/43720/disable-wifi-wlan0-on-pi-3
