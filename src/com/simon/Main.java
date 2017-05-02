package com.simon;


import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.wiringpi.GpioUtil;
import com.simon.max1704x_lipo_gauge.max17043;
import com.simon.sonos.*;
import com.simon.spotify.*;

import com.simon.ui2.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Main {
    public static String ipAddress = "";
    static String clientId = "";
    static String clientSecret = "";
    static String userid = "";

    public static ArrayList<item> activeList;
    public static ArrayList<item> queuePreBuf;
    public static ArrayList<item> SpotifyPreBuf;
    public static String[] firstList = {"radio", "queue", "Spotify", "tv"};
    public static ArrayList<deviceDescription> deviceList;
    public static int firstListFlag = -2;
    public static Sonos sonos = null;
    public static frame gui;
    public static ArrayList<item> radioStationList;
    static Spotify spotify;
    public static GpioPinDigitalInput ButtonLeft, ButtonRight, ButtonUp, ButtonDown, ButtonA, ButtonB, ButtonSelect, ButtonStart;
    public static GpioPinDigitalOutput ScreenPin;
    public static GpioController gpio;
    public static boolean fullscreen = true;
    public static int debounceTime = 500;
    public static Timer screenTimer;
    public static boolean Pi4jActive = false;
    public static final int defaultBatteryShutdownSOC = 3;
    public static boolean wifiState = true;
    public static ArrayList<Timer> timers;
    public static boolean wifiManager=false;
    public static Thread DeviceLisenter;
    public static ADS1115Controler ads1115Controler;
    public static volatile boolean isCharing = false;

    public static void main(String[] args) throws InterruptedException {

        timers = new ArrayList<>();
//            Shutdown shutdown = new Shutdown();
        try {
            Runtime.getRuntime().addShutdownHook(new ShutdownThread());
            System.out.println("[Main thread] Shutdown hook added");
        } catch (Throwable t) {
//             we get here when the program is run with java
//             version 1.2.2 or older
            System.out.println("[Main thread] Could not add Shutdown hook");
        }
        ui();
        Thread.sleep(5000);
        setFirstListGui();
        gui.Infopanel.LoadingIconOFF();

        wifiOn();


        System.getProperties().list(System.out);
//
        try {
            new systeminfoPrint();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        ipAddress = args[0];
        clientId = args[0];
        clientSecret = args[1];
        userid = args[2];
        if(args[3].contains("true")||args[3].contains("1")){
            fullscreen=true;
        }
        if(args[4].contains("true")||args[4].contains("1")){
            wifiManager=true;
        }



        spotify = new Spotify(clientId, clientSecret, userid);

        activeList = new ArrayList<>();
        gpio = null;



        Timer timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                System.out.println("time : " + sdf.format(cal.getTime()));

                if(ads1115Controler!=null){
                    isCharing =ads1115Controler.isCharing;

                }

            }
        });
        timers.add(timer);
        timer.start();
//        Timer wifi = new Timer(90000, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (wifiState) {
//                    wifiOn();
//                    wifiState = false;
//                } else {
//                    wifiOff();
//                    wifiState = true;
//                }
//
//            }
//        });
//        wifi.start();


        try {
            updateDevicelink();

//
        } catch (Exception e) {
            e.printStackTrace();
        }

            screenTimer = new Timer(60000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (Pi4jActive){
                        ScreenPin.high();
                        ads1115Controler.disable();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                   wifiOff();
                }
            }
            });
            timers.add(screenTimer);
            screenTimer.start();
            Thread.sleep(5000);
            if (!System.getProperty("os.name").contains("Windows")) {
                Pi4jActive = true;
            }
            if (Pi4jActive) {
                pi4jSetup();
                LowPowerCalls.turnOffOnbordLeds();

                GpioUtil.enableNonPrivilegedAccess();

                    try {
                        ads1115Controler = new ADS1115Controler();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (I2CFactory.UnsupportedBusNumberException e) {
                        e.printStackTrace();
                    }

            }


//        while(true){
//            Thread.sleep(10000);
//        }

    }

    public static void updateDevicelink() {
        DeviceLisenter = new Thread(new Runnable() {
            @Override
            public void run() {
                gui.Infopanel.LoadingIconON();
                Upnp_Discover discover = new Upnp_Discover();
                discover.start();
                while(discover.isAlive()){
                    ArrayList<deviceDescription> list = discover.getDevices();
                    for(int i =0;i<list.size();i++){
                        System.out.println("returned stuff : "+list.get(i).getIp());
                        if(list.get(i).getName()==null){
                            try {
                                list.set(i, Sonos.getZoneGroupAttributes(list.get(i).getIp()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        setNewGuiListDevices(list);
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ArrayList<deviceDescription> list = discover.getDevices();
                for(int i =0;i<list.size();i++){
                    System.out.println("returned stuff : "+list.get(i).getIp());
                    if(list.get(i).getName()==null){
                        try {
                            list.set(i, Sonos.getZoneGroupAttributes(list.get(i).getIp()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                setNewGuiListDevices(list);
                gui.Infopanel.LoadingIconOFF();
            }

        });
        DeviceLisenter.start();
    }


    public static void wifiOff(){

        if(Pi4jActive && wifiManager) {
            wifiState = false;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    String[] args = new String[]{"/bin/bash", "-c", "sudo iwconfig wlan0 txpower off"};
                    try {
                        Process proc = new ProcessBuilder(args).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("turning wifi off");
                }
            });
            t.start();
        }
    }
    public static void wifiOn() {

        if(Pi4jActive && wifiManager) {
            wifiState = true;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    String[] args = new String[]{"/bin/bash", "-c", "sudo iwconfig wlan0 txpower 1"};
                    try {
                        Process proc = new ProcessBuilder(args).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("turning wifi on");
                }
            });
            t.start();
        }
    }


    private static void pi4jSetup() {

//        try {
//            Robot  r = new Robot();
//        } catch (AWTException e) {
//            e.printStackTrace();
//        }
        final GpioController instance = GpioFactory.getInstance();

        gpio = instance;



        pi4jButtonLeftSetup(); // gpio 02
        pi4jButtonRightSetup(); // gpio 00
        pi4jButtonUpSetup(); // gpio 10
        pi4jButtonDownSetup(); // gpio 04
        pi4jButtonASetup(); //gpio 06
        pi4jButtonBSetup(); // gpio 5
        pi4jButtonStartSetup(); // gpio 11
        pi4jButtonSelectSetup(); // gpio 07
        pi4jScreenControler(); // gpio 8 and 9

        try {
            max17043 max = new max17043(I2CFactory.getInstance(I2CBus.BUS_1));
            max.clearAlertInterrupt();
            max.setAlertThreshold(defaultBatteryShutdownSOC);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public static void screenToogle(){
      ScreenPin.toggle();
        System.out.println("scrren is : " + ScreenPin.getState());
    }
    private static void pi4jScreenControler() {
       ScreenPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
       ScreenPin.setShutdownOptions(true,PinState.LOW);
        System.out.println("screen pin set");
    }

    public static void pi4jButtonSelectSetup(){
        ButtonSelect = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN);
        ButtonSelect.setShutdownOptions(true);
        ButtonSelect.setDebounce(debounceTime);
        ButtonSelect.addListener(new GpioPinListenerDigital() {

            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                try {
                    Robot  r = new Robot();
                    if(event.getState().isHigh()) {
                        r.keyPress(KeyEvent.VK_D);
                    }else {
                        r.keyRelease(KeyEvent.VK_D);
                    }
                } catch (AWTException e) {
                    e.printStackTrace();
                }
//
            }

        });
    }
    public static void pi4jButtonStartSetup(){
        ButtonStart = gpio.provisionDigitalInputPin(RaspiPin.GPIO_11, PinPullResistance.PULL_DOWN);
        ButtonStart.setShutdownOptions(true);
        ButtonStart.setDebounce(debounceTime);
        ButtonStart.addListener(new GpioPinListenerDigital() {

            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                try {
                    Robot  r = new Robot();
                    if(event.getState().isHigh()) {
                        r.keyPress(KeyEvent.VK_S);
                    }else {
                        r.keyRelease(KeyEvent.VK_S);
                    }
                } catch (AWTException e) {
                    e.printStackTrace();
                }

            }

        });
    }
    public static void pi4jButtonBSetup(){
        ButtonB = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN);
        ButtonB.setShutdownOptions(true);
        ButtonB.setDebounce(debounceTime);
        ButtonB.addListener(new GpioPinListenerDigital() {

            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                try {
                    Robot  r = new Robot();
                    if(event.getState().isHigh()) {
                        r.keyPress(KeyEvent.VK_LEFT);
                    }else {
                        r.keyRelease(KeyEvent.VK_LEFT);
                    }
                } catch (AWTException e) {
                    e.printStackTrace();
                }

            }

        });
    }
    public static void pi4jButtonASetup(){
        ButtonA = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, PinPullResistance.PULL_DOWN);
        ButtonA.setShutdownOptions(true);
        ButtonA.setDebounce(debounceTime);
        ButtonA.addListener(new GpioPinListenerDigital() {

            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                try {
                    Robot  r = new Robot();
                    if(event.getState().isHigh()) {
                        r.keyPress(KeyEvent.VK_ENTER);
                    }else {
                        r.keyRelease(KeyEvent.VK_ENTER);
                    }
                } catch (AWTException e) {
                    e.printStackTrace();
                }
//
            }

        });
    }
    public static void pi4jButtonDownSetup(){
        ButtonDown = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);
        ButtonDown.setShutdownOptions(true);
        ButtonDown.setDebounce(debounceTime);
        ButtonDown.addListener(new GpioPinListenerDigital() {

            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                try {
                    Robot  r = new Robot();
                    if(event.getState().isHigh()) {
                        r.keyPress(KeyEvent.VK_DOWN);
                    }else {
                        r.keyRelease(KeyEvent.VK_DOWN);
                    }
                } catch (AWTException e) {
                    e.printStackTrace();
                }

            }

        });
    }
    public static void pi4jButtonUpSetup(){
//        ButtonUp = gpio.provisionDigitalInputPin(RaspiPin.GPIO_08, PinPullResistance.PULL_DOWN);
        ButtonUp = gpio.provisionDigitalInputPin(RaspiPin.GPIO_10);
        ButtonUp.setShutdownOptions(true);
        ButtonUp.setDebounce(debounceTime);
        ButtonUp.addListener(new GpioPinListenerDigital() {

            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                try {
                    Robot  r = new Robot();
                    if(event.getState().isHigh()) {
                        r.keyPress(KeyEvent.VK_UP);
                    }else {
                        r.keyRelease(KeyEvent.VK_UP);
                    }
                } catch (AWTException e) {
                    e.printStackTrace();
                }

            }

        });
    }
    public static void pi4jButtonRightSetup(){
        ButtonRight = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN);
        ButtonRight.setShutdownOptions(true);
        ButtonRight.setDebounce(debounceTime);
        ButtonRight.addListener(new GpioPinListenerDigital() {

            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                try {
                    Robot  r = new Robot();
                    if(event.getState().isHigh()) {
                        r.keyPress(KeyEvent.VK_RIGHT);
                    }else {
                        r.keyRelease(KeyEvent.VK_RIGHT);
                    }
                } catch (AWTException e) {
                    e.printStackTrace();
                }

            }

        });
    }
    public static void pi4jButtonLeftSetup(){
        ButtonLeft = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
        ButtonLeft.setShutdownOptions(true);
        ButtonLeft.setDebounce(debounceTime);


        ButtonLeft.addListener((GpioPinListenerDigital) event -> {
//                 display pin state on console
            System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            try {
                Robot  r = new Robot();
                if(event.getState().isHigh()) {
                    r.keyPress(KeyEvent.VK_LEFT);
                }else {
                    r.keyRelease(KeyEvent.VK_LEFT);
                }
            } catch (AWTException e) {
                e.printStackTrace();
            }

        });
    }

    private static void ui() {

        // new FullScreenJFrame();

        // gui = new gui2(false);

         gui = new frame(fullscreen);
    }

    public static void getSpotifyPlayLists(){
        setNewGuiList(SpotifyPreBuf);
        Thread t = new Thread(){
            @Override
            public synchronized void start() {
                super.start();
                gui.Infopanel.LoadingIconON();
                setNewGuiList(Spotify.GetPublicPlayLists());
                gui.Infopanel.LoadingIconOFF();
            }
        };
        t.start();
    }
    public static void getQueue() {
        setNewGuiList(queuePreBuf);

        Sonos s = sonos;

            Thread t = new Thread() {
                @Override
                public void run() {
                    super.run();
                    gui.Infopanel.LoadingIconON();
                    try {
                        activeList = s.BrowseQueue(0, 0, 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setNewGuiList(activeList);
                    queuePreBuf = activeList;
                    firstListFlag = 1;
                    gui.Infopanel.LoadingIconOFF();
                }
        };
            t.start();

    }
    public static void getMusicFolders(){
        browse data = null;
        try {
            data = sonos.BrowseMusicFolders();
         setNewGuiList(sonos.ItemAnalyser(data.container.get(0).containerId));
    } catch (IOException e) {
        e.printStackTrace();
        }
    }

    public static void setNewGuiList(ArrayList<item> itemList) {
        Thread t = new Thread(){
            @Override
            public void run() {
                if(itemList!=null) {
                    String[] newlist = new String[itemList.size()];
                    for (int i = 0; i < itemList.size(); i++) {
                        newlist[i] = itemList.get(i).title;
                    }
                    activeList = itemList;
                    frame.newList(newlist);
                }

            }
        };

        t.start();
    }
    public static void setNewGuiListDevices(ArrayList<deviceDescription> itemList) {
        Thread t = new Thread(){
            @Override
            public void run() {
                if(itemList!=null) {

                    String[] newlist = new String[itemList.size()];
                    for (int i = 0; i < itemList.size(); i++) {


                            newlist[i] = itemList.get(i).getIp() + " - " + itemList.get(i).getName();

                    }
                    deviceList = itemList;
                    frame.newList(newlist);
                }

            }
        };

        t.start();
    }

    public static void setFirstListGui() {

//        frame.newList(firstList);
        setNewGuiListDevices(deviceList);
    }
    public static void setSecondListGui() {

        frame.newList(firstList);
//        setNewGuiListDevices(deviceList);
    }
    public static void shutdown(){
        try {
            gpio.shutdown();


            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("auto shutdown do to low battery to protect the battery");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            String[] args = new String[] {"/bin/bash", "-c", "sudo shutdown -h now"};
            Process proc = new ProcessBuilder(args).start();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void DisplatOFF(){
        try {
            String[] args = new String[] {"/bin/bash", "-c", "tvservice -o"};
            Process proc = new ProcessBuilder(args).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void DisplatON(){
        try {
            String[] args = new String[] {"/bin/bash", "-c", "tvservice -c \"PAL 4:3\""};
            Process proc = new ProcessBuilder(args).start();
            args = new String[] {"/bin/bash", "-c", "sudo service lightdm restart"};
            new ProcessBuilder(args).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean getRadioList() {
        try {
            activeList = sonos.BrowseRadioStations();
            if(activeList!=null) {
                setNewGuiList(activeList);
                return true;
            }else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void updateSonosLink(String ip) {
        Main.ipAddress = ip;
        gui.Infopanel.LoadingIconON();
        sonos = new Sonos(ipAddress);

        preBuf();

    }
    private static void preBuf(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    queuePreBuf = sonos.BrowseQueue(0, 0, 0);

                    if(queuePreBuf==null){
                        queuePreBuf = new ArrayList<>();

                    }

                    browse data = sonos.BrowseMusicFolders();
                    if(data!=null) {
                        sonos.ItemAnalyser(data.container.get(0).containerId).get(0).print();
                    }
                    SpotifyPreBuf = spotify.GetPublicPlayLists();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gui.Infopanel.LoadingIconOFF();
            }
        });
        t.start();

    }
}