package com.simon.ui2;

import com.simon.Main;
import com.simon.sonos.Sonos;
import com.simon.spotify.Spotify;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;

/**
 * Created by simon on 3/14/2017.
 */

// loading gif generator http://www.ajaxload.info/

public class InfoPanel extends JPanel {
    final JProgressBar pb;
    JLabel title;
    JLabel wIcon;
    public InfoPanel() {

        Dimension size = getPreferredSize();
        size.height = 50;
        setPreferredSize(size);

        setBorder(BorderFactory.createTitledBorder(" "));
        JLabel LabelTitle = new JLabel("Title");

        JLabel LabelArtist = new JLabel("Artist");

        title = new JLabel("test title");
        JLabel Artist = new JLabel("test arTist");



        URL yrl =this.getClass().getResource("ajax-loader.gif");
       // ImageIcon i = new ImageIcon(yrl).setImage(new ImageIcon(yrl).getImage().getScaledInstance(10,10,Image.SCALE_DEFAULT));

        wIcon = new JLabel(new ImageIcon(yrl));
        // JLabel wIcon = new JLabel(icon);


        pb = new JProgressBar();
        pb.setMinimum(0);
        pb.setMaximum(100);
        pb.setStringPainted(true);
        pb.setString("hej");

        //pb.setIndeterminate(true);
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

     //   gc.anchor = GridBagConstraints.LINE_START;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx =0;
        gc.gridy =0;

      //  add(LabelTitle,gc);

        gc.gridx =0;
        gc.gridy =3;

      //  add(LabelArtist,gc);

//        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 1;
        gc.gridy =1;
        add(title,gc);

        gc.gridx = 1;
        gc.gridy =2;

        gc.fill = GridBagConstraints.HORIZONTAL;
        add(pb,gc);

        gc.fill = GridBagConstraints.VERTICAL;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.gridx = 2;
        gc.gridy = 1;

        add(wIcon,gc);


        updatInfoPanel();

    }
    public int timeformatToInt(String time){
        String[] units = time.split(":"); //will break the string up into an array
        int houres = Integer.parseInt(units[0]); //first element
        int minutes = Integer.parseInt(units[1]); //first element
        int seconds = Integer.parseInt(units[2]); //second element
       return houres*3600+60 * minutes + seconds; //add up our values
    }
    public void updatInfoPanel() {


        int timeinSeckunds = 0;
        int position = 0;
        try {
            Sonos.posisitionInfo data = Sonos.getPosistionInfo();
            String time = data.Duratation; //mm:ss

            timeinSeckunds = timeformatToInt(data.Duratation);
            position = timeformatToInt(data.RelTime);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String name = "DAD";


        pb.setMaximum(timeinSeckunds);

        title.setText(name);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Timer t = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (Main.sonos.getTransportInfo()) {
                                pb.setValue(pb.getValue() + 1);
                                pb.setString(LocalTime.MIN.plusSeconds(pb.getMaximum() - pb.getValue()).toString());
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }
                });
                t.start();

            }
        });
    }
    public void toggleLoadingIcon(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(wIcon.isVisible()){
                    wIcon.setVisible(false);


                }else {
                    wIcon.setVisible(true);
                }
            }
        });

    }
    public void LoadingIconON(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                    wIcon.setVisible(true);

            }
        });

    }
    public void LoadingIconOFF(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                wIcon.setVisible(false);

            }
        });

    }
}
