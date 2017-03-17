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

```

### runnable .sh file no terminal window

```sh
#!/bin/bash

sudo nohup java -jar "path to youre jar "/gameboy-sonos-remotes.jar sonosip clientid clientsecretKey userid fullscreenMode

```
### runnable .sh file with terminal window

```sh
#!/bin/bash

sudo java -jar "path to youre jar "/gameboy-sonos-remotes.jar sonosip clientid clientsecretKey userid fullscreenMode

```

### auto .sh file on boot

still unkown
