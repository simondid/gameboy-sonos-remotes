package com.simon.sonos;

import com.simon.Main;
import com.simon.ui.gui2;

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
    public keyevent() {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        gui2.list.ensureIndexIsVisible(gui2.list.getSelectedIndex());
        Sonos sonos = Main.sonos;


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
        if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
            System.out.println("index : "+gui2.list.getSelectedIndex() + " flag : "+ Main.firstListFlag);
            switch (Main.firstListFlag){
                case -1:

                    System.out.println("first list action");
                    switch (gui2.list.getSelectedIndex()) {
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

                        int i = sonos.playUri(Main.activeList.get(gui2.list.getSelectedIndex()));
                        System.out.println(i);
                        sonos.Seek_track_nr(i);

                        sonos.play();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                case 1:
                    // handling queue
                    try {
                        sonos.SetPlayListAsInput();

                        sonos.Seek_track_nr(gui2.list.getSelectedIndex()+1);

                        sonos.play();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    break;
                case 2:



                    try {
                        switch ( gui2.popup(Main.gui)){
                            case 0:
                                // add
                                sonos.SetPlayListAsInput();

                                sonos.playSpotifyPlayList(Main.activeList.get(gui2.list.getSelectedIndex()));
                                break;
                            case 1:
                                // replace
                                sonos.RemoveAllTracks();
                                sonos.SetPlayListAsInput();
                                sonos.playSpotifyPlayList(Main.activeList.get(gui2.list.getSelectedIndex()));
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

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
