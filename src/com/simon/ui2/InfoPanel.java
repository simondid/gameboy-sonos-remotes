package com.simon.ui2;

import com.oracle.deploy.update.UpdateInfo;
import com.simon.Main;
import com.simon.sonos.Sonos;
import com.simon.sonos.item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.concurrent.*;

/**
 * Created by simon on 3/14/2017.
 */

// loading gif generator http://www.ajaxload.info/

public class InfoPanel extends JPanel {
    final JProgressBar pb;
    JLabel title;
    JLabel wIcon;
    Timer t;
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




        t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //try {


                    //  if (Main.sonos.getTransportInfo()) {
                    if(isPlaying){
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {

                                pb.setValue(pb.getValue() + 1);
                                pb.setString(LocalTime.MIN.plusSeconds(pb.getMaximum() - pb.getValue()).toString());
                            }
                        });

                }
                if (pb.getMaximum() == pb.getValue()) {
                   // updatInfoPanel(null);
                }else{
                    try {
                        isPlaying = new getTransportState().call();
                        new getPositionInfo().call();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });
        t.start();
     //   updatInfoPanel(null);
    }

    public int timeformatToInt(String time){
        String[] units = time.split(":"); //will break the string up into an array
        int houres = Integer.parseInt(units[0]); //first element
        int minutes = Integer.parseInt(units[1]); //first element
        int seconds = Integer.parseInt(units[2]); //second element
       return houres*3600+60 * minutes + seconds; //add up our values
    }
    volatile boolean isPlaying = true;
    public void updatInfoPanel(item input) {


        int timeinSeckunds = 0;
        int position = 0;

        try {
            Sonos.posisitionInfo data = Sonos.getPosistionInfo();

            timeinSeckunds = timeformatToInt(data.Duratation);
            position = timeformatToInt(data.RelTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String name="NAN";
        if(input == null){

        }else {
            name = input.title;
        }

        pb.setValue(position);
        pb.setMaximum(timeinSeckunds);

        title.setText(name);




                CountDownLatch l = new CountDownLatch(1);




//                Future<Boolean> task = executor.submit(new getTransportState());

//                try {
//                    if(task.isDone()) {
//                        result[0] = task.get();
//                    }
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                } catch (ExecutionException e1) {
//                    e1.printStackTrace();
//                }



    }

    class getPositionInfo implements Callable<Void>{

        @Override
        public Void call() throws Exception {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            Sonos s = new Sonos(Main.ipAddress);

            Sonos.posisitionInfo data = s.getPosistionInfo();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    pb.setMaximum(timeformatToInt(data.Duratation));
                    pb.setValue(timeformatToInt(data.RelTime));
//                    pb.setString(Main.activeList.get(data.track).title);
                    title.setText(Main.queuePreBuf.get(data.track-1).title);
                    updatInfoPanel(Main.queuePreBuf.get(data.track-1));
                }
            });

            return null;
        }
    }
    class getTransportState implements Callable<Boolean> {


        @Override
        public Boolean call() throws Exception {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            Sonos s = new Sonos(Main.ipAddress);

            boolean data = s.getTransportInfo();
            return data;


        }
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
