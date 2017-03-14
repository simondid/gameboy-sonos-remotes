package com.simon.ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by simon on 3/3/2017.
 */
public class FullScreenJFrame extends JFrame {

    private GraphicsDevice vc;

    public FullScreenJFrame() {
        super();

        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        vc = e.getDefaultScreenDevice();


        JButton b = new JButton("exit");
        b.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        dispose();
                        System.exit(0);

                    }
                }
        );
        this.setLayout(new FlowLayout());
        this.add(b);

        String[] data = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};
        JList list = new JList(data);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
               if(!listSelectionEvent.getValueIsAdjusting()){
                   System.out.println(listSelectionEvent.getLastIndex());

               }
            }
        });
        this.add(list);

        setFullScreen(this);
    }

    public void setFullScreen(JFrame f) {

        f.setUndecorated(true);
        f.setResizable(false);
        vc.setFullScreenWindow(f);


    }
}