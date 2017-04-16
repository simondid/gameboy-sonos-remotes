package com.simon;

import com.simon.Main;
import com.simon.sonos.Sonos;
import com.simon.ui.gui2;
import com.simon.ui2.frame;
import com.simon.ui2.listPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by simon on 3/6/2017.
 */
public class keyevent implements KeyListener {
    private final Set<Character> pressed = new HashSet<Character>();
    static Sonos sonos;
    private boolean VolLeverTimer = false;
    Timer VolLevelChangeTimer;
    int volume =-1;
    public keyevent() {
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        screenhandler();
        Main.gui.listPanel.list.ensureIndexIsVisible(Main.gui.listPanel.list.getSelectedIndex());

        sonos = Main.sonos;

        System.out.println("keyEvent =" + e.getKeyCode());


        VolLevelChangeTimer = new Timer(30000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                VolLeverTimer = false;
//                System.out.println(VolLeverTimer);
            }
        });
        VolLevelChangeTimer.setRepeats(false);






        switch (e.getKeyCode()){
            case KeyEvent.VK_C:
                try {
                    sonos.RemoveAllTracks();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            case KeyEvent.VK_LEFT:
                Main.setSecondListGui();
                Main.firstListFlag =-1;
                break;
            case KeyEvent.VK_RIGHT:
                selectionHandler();
                break;
            case KeyEvent.VK_ENTER:
                selectionHandler();
                break;
            case KeyEvent.VK_S:
                Main.updateDevicelink();
                Main.setFirstListGui();
                Main.firstListFlag = -2;
                break;
            case KeyEvent.VK_PLUS:

                try {

                    if(VolLeverTimer) {
                        setGroupVolume(5);

                    }else{

                        volume = sonos.getGroupVolume(0)+5;
                        sonos.setGroupVolume(volume);
                        VolLeverTimer=true;
                        VolLevelChangeTimer.start();
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            case KeyEvent.VK_ADD:

                try {

                    if(VolLeverTimer) {
                        setGroupVolume(5);

                    }else{

                        volume = sonos.getGroupVolume(0)+5;
                        sonos.setGroupVolume(volume);
                        VolLeverTimer=true;
                        VolLevelChangeTimer.start();
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }                break;
            case KeyEvent.VK_MINUS:
                try {

                    if(VolLeverTimer) {
                        setGroupVolume(-5);

                    }else{

                        volume = sonos.getGroupVolume(0)-2;
                        sonos.setGroupVolume(volume);
                        VolLeverTimer=true;
                        VolLevelChangeTimer.start();
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                case KeyEvent.VK_SUBTRACT:
                    try {

                        if(VolLeverTimer) {
                            setGroupVolume(-5);

                        }else{

                            volume = sonos.getGroupVolume(0)-2;
                            sonos.setGroupVolume(volume);
                            VolLeverTimer=true;
                            VolLevelChangeTimer.start();
                        }

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                 break;

        }

//        if(e.getKeyCode()==KeyEvent.VK_C){
//            try {
//                sonos.RemoveAllTracks();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//
//        }
//        if(e.getKeyCode()==KeyEvent.VK_LEFT){
//            Main.setSecondListGui();
//            Main.firstListFlag =-1;
//        }
//        if(e.getKeyCode()==KeyEvent.VK_RIGHT||e.getKeyCode()==KeyEvent.VK_ENTER) {
//
//                selectionHandler();
//
//        }
//        if(e.getKeyCode()==KeyEvent.VK_S||e.getKeyCode()==KeyEvent.VK_S){
//            Main.updateDevicelink();
//            Main.setFirstListGui();
//            Main.firstListFlag = -2;
//        }
//        if(e.getKeyCode()==KeyEvent.VK_PLUS || e.getKeyCode()==KeyEvent.VK_ADD){
//            int volume =-1;
//            try {
//                volume = sonos.getGroupVolume(0);
//                sonos.setGroupVolume(volume+5);
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
//        if(e.getKeyCode()==KeyEvent.VK_MINUS || e.getKeyCode()==KeyEvent.VK_SUBTRACT){
//            int volume =-1;
//            try {
//                volume = sonos.getGroupVolume(0);
//                sonos.setGroupVolume(volume-5);
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
    }
    private void setGroupVolume(int i ) throws IOException {
        volume = volume +i;
        if(!sonos.setGroupVolume(volume)){
            if(volume>99){
                volume=99;
            }
            if(volume<0){
                volume=0;
            }
        }

    }
    private static void screenhandler(){
        System.out.println("screen timer reset");
//        if(Main.ScreenPin.isLow()) {
        if(Main.Pi4jActive) {
            Main.wifiOn();
            if(Main.ScreenPin.isHigh()) {
                Main.ScreenPin.low();
            }
            Main.screenTimer.restart();
        }
//        }else{
//            Main.ScreenPin.low();
//        }
    }
    public static void selectionHandler(){
        screenhandler();
        System.out.println("index : "+Main.gui.listPanel.list.getSelectedIndex() + " flag : "+ Main.firstListFlag);
        switch (Main.firstListFlag){
            case -2:
                Main.DeviceLisenter.stop();
//                Main.ipAddress = Main.deviceList.get(Main.gui.listPanel.list.getSelectedIndex()).getIp();
                Main.updateSonosLink(Main.deviceList.get(Main.gui.listPanel.list.getSelectedIndex()).getIp());
                Main.setSecondListGui();
                Main.firstListFlag = -1;
                break;
            case -1:

                System.out.println("first list action");
                switch (frame.listPanel.list.getSelectedIndex()) {
                    case 0:


                        if(Main.getRadioList()) {

                            Main.firstListFlag = 0;
                        }
                        break;
                    case 1:
                        Main.firstListFlag =1;
                        Main.getQueue();
                        break;
                    case 2:
                        Main.firstListFlag =2;
                        Main.getSpotifyPlayLists();
                        break;
                    case 3:
                        try {
                            sonos.SetTvAsInput();
                            sonos.play();
                        } catch (IOException e1) {

                        }
                        break;

                }
                break;

            case 0:
                // handling radio stations
                try {

                    int i = sonos.AddURIToQueue(Main.activeList.get(Main.gui.listPanel.list.getSelectedIndex()));
                    System.out.println(i);
                    sonos.Seek_track_nr(i);

                    sonos.play();
                    sonos.SetAvTransportURI(Main.activeList.get(Main.gui.listPanel.list.getSelectedIndex()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            case 1:
                // handling queue
                try {
                    sonos.SetPlayListAsInput();

                    int position = Main.gui.listPanel.list.getSelectedIndex();
                    sonos.Seek_track_nr(position+1);

                    sonos.play();
                    Main.gui.Infopanel.updatInfoPanel(Main.activeList.get(position));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                break;
            case 2:



                try {
                    switch (frame.popup(Main.gui)){
                        case 0:
                            // add
                            sonos.SetPlayListAsInput();

                            sonos.playSpotifyPlayList(Main.activeList.get(Main.gui.listPanel.list.getSelectedIndex()));
                            break;
                        case 1:
                            // replace
                            sonos.RemoveAllTracks();
                            sonos.SetPlayListAsInput();
                            sonos.playSpotifyPlayList(Main.activeList.get(Main.gui.listPanel.list.getSelectedIndex()));
                            sonos.play();
                            break;
                        case 2:
                            //cancel

                            break;

                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
        }


    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
