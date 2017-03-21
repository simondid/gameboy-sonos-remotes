package com.simon;

import com.simon.Main;
import com.simon.sonos.Sonos;
import com.simon.ui.gui2;
import com.simon.ui2.frame;
import com.simon.ui2.listPanel;

import java.awt.*;
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
    public keyevent() {
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Main.gui.listPanel.list.ensureIndexIsVisible(Main.gui.listPanel.list.getSelectedIndex());

        sonos = Main.sonos;


        if(e.getKeyCode()==KeyEvent.VK_C){
            try {
                sonos.RemoveAllTracks();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            Main.setFirstListGui();
            Main.firstListFlag =-1;
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT||e.getKeyCode()==KeyEvent.VK_ENTER) {

                selectionHandler();

        }

    }

    public static void selectionHandler(){

        System.out.println("index : "+Main.gui.listPanel.list.getSelectedIndex() + " flag : "+ Main.firstListFlag);
        switch (Main.firstListFlag){
            case -1:

                System.out.println("first list action");
                switch (frame.listPanel.list.getSelectedIndex()) {
                    case 0:

                        Main.firstListFlag =0;
                        Main.getRadioList();
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
                            e1.printStackTrace();
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
