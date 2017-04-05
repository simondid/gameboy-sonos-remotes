package com.simon;


import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;
import com.pi4j.io.gpio.trigger.GpioTrigger;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.system.SystemInfo;
import com.simon.max1704x_lipo_gauge.max17043;
import com.simon.sonos.*;
import com.simon.spotify.*;

import com.simon.ui2.InfoPanel;
import com.simon.ui2.frame;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Callable;

public class Main{
    public static String ipAddress = "";
    static String clientId = "";
    static String clientSecret = "";
    static String userid = "";

    public static ArrayList<item> activeList;
    public static ArrayList<item> queuePreBuf;
    public static ArrayList<item> SpotifyPreBuf;
    public static String[] firstList = {"radio","queue","Spotify","tv"};
    public static int firstListFlag = -1;
    public static Sonos sonos;
    public static frame gui;
    public static ArrayList<item> radioStationList;
    static Spotify spotify;
    public static GpioPinDigitalInput ButtonLeft,ButtonRight,ButtonUp,ButtonDown,ButtonA,ButtonB,ButtonSelect,ButtonStart;
    public static GpioPinDigitalOutput ScreenPin;
    public static GpioController gpio;
    public static String fullscreen = "";
    public static int debounceTime = 500;
    public static Timer screenTimer;
    public static boolean Pi4jActive = false;
    public static void main(String[] args) throws InterruptedException {

        System.getProperties().list(System.out);

//        try {
//            new systeminfoPrint();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
        ipAddress = args[0];
        clientId = args[1];
        clientSecret = args[2];
        userid = args[3];
        fullscreen = args[4];


        spotify = new Spotify(clientId,clientSecret,userid);
        sonos = new Sonos(ipAddress);
        activeList = new ArrayList<>();
        gpio=null;
        sonos = new Sonos(ipAddress);


        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                System.out.println( "time : "+sdf.format(cal.getTime()) );

            }
        });
        timer.start();



        try {

//         sonos.getTransportInfo();
      //      sonos.setGroupVolume(55);

      //      sonos.Next();

          //  sonos.Stop();

            //sonos.Previous();


            //sonos.play();

            queuePreBuf = sonos.BrowseQueue(0,0,0);




            browse data =sonos.BrowseMusicFolders();

            sonos.ItemAnalyser(data.container.get(0).containerId).get(0).print();
       //     itemList = sonos.ItemAnalyser(data.container.get(0).containerId);
          //  sonos.playUri(itemList.get(0));

           /*
            for(int i =0;i<itemList.size();i++){
                gui.addItem(itemList.get(i).title);
            }
*/


         //   gui.clearList();

           // itemList = sonos.BrowseRadioStations();
        /*    for(int i =0;i<itemList.size();i++){
                gui.addItem(itemList.get(i).title);
            }*/


            ui();

            setFirstListGui();
            gui.Infopanel.LoadingIconOFF();

          // sonos.playSpotifyPlayList(SpotifiUser,SpotifiPlayList,SpotifyPlayListName);


         /*   new Runnable() {
                @Override
                public void run() {
                    DisplatOFF();

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    DisplatON();

                }
            }.run();

*/


        } catch (Exception e) {
        e.printStackTrace();
    }
//        try {
//            new VolumeControle();
        SpotifyPreBuf = spotify.GetPublicPlayLists();
        screenTimer = new Timer(60000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ScreenPin.high();
            }
        });
        screenTimer.start();
        Thread.sleep(15000);
//        try {
//            new systeminfoPrint();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        if(!System.getProperty("os.name").contains("Windows")){
            Pi4jActive = true;
        }
        if(Pi4jActive) {
            pi4jSetup();
            LowPowerCalls.turnOffOnbordLeds();


        }





//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (I2CFactory.UnsupportedBusNumberException e) {
//            e.printStackTrace();
//        }


//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//        t.run();



    }


    private static void pi4jSetup() {

//        try {
//            Robot  r = new Robot();
//        } catch (AWTException e) {
//            e.printStackTrace();
//        }
        final GpioController instance = GpioFactory.getInstance();

        gpio = instance;

        try {
            max17043 max = new max17043(I2CFactory.getInstance(I2CBus.BUS_1));
            max.setAlertThreshold(3);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            e.printStackTrace();
        }


//        pi4jButtonLeftSetup(); // gpio 02
//        pi4jButtonRightSetup(); // gpio 00
//        pi4jButtonUpSetup(); // gpio 08
//        pi4jButtonDownSetup(); // gpio 04
//        pi4jButtonASetup(); //gpio 05
//        pi4jButtonBSetup();
//        pi4jButtonStartSetup(); // gpio 06
//        pi4jButtonSelectSetup(); // gpio 07
        pi4jScreenControler();


    }

    public static void screenToogle(){
      ScreenPin.toggle();
        System.out.println("scrren is : " + ScreenPin.getState());
    }
    private static void pi4jScreenControler() {
       ScreenPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
       ScreenPin.setShutdownOptions(false);
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
                        r.keyPress(KeyEvent.VK_LEFT);
                    }else {
                        r.keyRelease(KeyEvent.VK_LEFT);
                    }
                } catch (AWTException e) {
                    e.printStackTrace();
                }
//
            }

        });
    }
    public static void pi4jButtonStartSetup(){
        ButtonStart = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, PinPullResistance.PULL_DOWN);
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
        ButtonA = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN);
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
        ButtonUp = gpio.provisionDigitalInputPin(RaspiPin.GPIO_08);
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

        Sonos s = new Sonos(ipAddress);

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
                String[] newlist = new String[itemList.size()];
                for(int i = 0;i< itemList.size();i++){
                    newlist[i]=itemList.get(i).title;
                }
                activeList = itemList;
                frame.newList(newlist);

            }
        };

        t.start();
    }

    public static void setFirstListGui() {

        frame.newList(firstList);
    }

    public static void shutdown(){
        try {
            gpio.shutdown();


            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("auto shutdown do to low battery to protect the battery");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            String[] args = new String[] {"/bin/bash", "-c", "sudo shutdown"};
            Process proc = new ProcessBuilder(args).start();

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


    public static void getRadioList() {
        try {
            activeList = sonos.BrowseRadioStations();
            setNewGuiList(activeList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}