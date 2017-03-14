package com.simon;


import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.simon.sonos.*;
import com.simon.spotify.*;
import com.simon.ui.gui2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

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
    public static JFrame gui;
    public static ArrayList<item> radioStationList;
    static Spotify spotify;
    public static GpioPinDigitalInput ButtonLeft,ButtonRight,ButtonUp,ButtonDown,ButtonA,ButtonB,ButtonSelect,ButtonStart;
    static GpioController gpio;
    public static void main(String[] args){

        ipAddress = args[0];
        clientId = args[1];
        clientSecret = args[2];
        userid = args[3];

        spotify = new Spotify(clientId,clientSecret,userid);
        sonos = new Sonos(ipAddress);
        activeList = new ArrayList<>();
        gpio=null;

        try {




    //        sonos.getTransportInfo();
      //      sonos.setGroupVolume(55);

      //      sonos.Next();

          //  sonos.Stop();

            //sonos.Previous();


            //sonos.play();

            queuePreBuf = sonos.BrowseQueue(0,0,0);
            SpotifyPreBuf = spotify.GetPublicPlayLists();




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

            String SpotifiUser="114036704";
            String SpotifiPlayList="2RJZP5M6B4QGHkNgsjcri7";
            String SpotifyPlayListName="alt det gode";


          // sonos.playSpotifyPlayList(SpotifiUser,SpotifiPlayList,SpotifyPlayListName);

            pi4jSetup();


        } catch (IOException e) {
        e.printStackTrace();
    }

      //  System.exit(0);
    }

    private static void pi4jSetup() {

        try {
            Robot  r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        gpio = GpioFactory.getInstance();

        pi4jButtonLeftSetup(); // gpio 02
        pi4jButtonRightSetup(); // gpio 00
        pi4jButtonUpSetup(); // gpio 01
        pi4jButtonDownSetup(); // gpio 04
        pi4jButtonASetup(); //gpio 05
        pi4jButtonStartSetup(); // gpio 06
        pi4jButtonSelectSetup(); // gpio 07

    }
    public static void pi4jButtonSelectSetup(){
        ButtonSelect = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN);
        ButtonSelect.setShutdownOptions(true);

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

            }

        });
    }
    public static void pi4jButtonStartSetup(){
        ButtonStart = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, PinPullResistance.PULL_DOWN);
        ButtonStart.setShutdownOptions(true);

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

            }

        });
    }
    public static void pi4jButtonDownSetup(){
        ButtonDown = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);
        ButtonDown.setShutdownOptions(true);

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
        ButtonUp = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);
        ButtonUp.setShutdownOptions(true);

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

        ButtonLeft.addListener(new GpioPinListenerDigital() {

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

    private static void ui() {

        // new FullScreenJFrame();

         gui = new gui2(false);

    }

    public static void getSpotifyPlayLists(){
        setNewGuiList(SpotifyPreBuf);
        Thread t = new Thread(){
            @Override
            public synchronized void start() {
                super.start();
                setNewGuiList(Spotify.GetPublicPlayLists());
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
                    try {
                        activeList = s.BrowseQueue(0, 0, 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setNewGuiList(activeList);
                    queuePreBuf = activeList;
                    firstListFlag = 1;
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
                gui2.newList(newlist);

            }
        };

        t.start();
    }
    public static void setFirstListGui() {

        gui2.newList(firstList);
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