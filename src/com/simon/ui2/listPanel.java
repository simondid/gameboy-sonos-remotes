package com.simon.ui2;

import com.simon.Main;
import com.simon.keyevent;
import com.simon.sonos.Sonos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Created by simon on 3/14/2017.
 */
public class listPanel extends JPanel {

    public static JList<String> list = new JList<>();

    public listPanel() {
        String[] data = new String[100];
        for(int i =0;i<data.length;i++){
            data[i]=""+i;
        }

        setLayout(new BorderLayout());

        GridBagConstraints gc = new GridBagConstraints();

        gc.weighty =0.0;
        gc.weightx =0.5;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx =0;
        gc.gridy =0;
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList theList = (JList) e.getSource();

                if (e.getClickCount() == 2) {
                    int index = theList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        Object o = theList.getModel().getElementAt(index);
                        System.out.println("Double-clicked on: " +index +" : "+ o.toString());
                        try {
                            new Sonos(Main.ipAddress).Seek_track_nr(index+1);

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }


                }
            }
        });

        list.addKeyListener(new keyevent());
        list.setListData(data);


        add(new JScrollPane(list));

        this.setBackground(Color.cyan);
    }
}
