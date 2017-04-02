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

java -jar gameboy-sonos-remotes sonosip clientid clientsecretKey userid
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




## electronics wiring

### screen on / off controler
http://i63.tinypic.com/11jc6mq.png
http://electronics.stackexchange.com/questions/233205/npn-transistor-to-run-12v-0-5a-from-3-3v-4ma

gpio 1 aka pin 12 on the raspberry pi model b

![screen on / off controler](http://i63.tinypic.com/11jc6mq.png "screen on / off controler")


### battery management circuit based on the max17043 lipo fuel gauge
http://i65.tinypic.com/xdf0wi.png
https://github.com/simondid/MAX17043

the battery management code is gonna be based on my max17043 class

https://github.com/simondid/MAX17043


![battery circuit](http://i65.tinypic.com/xdf0wi.png "battery circuit")