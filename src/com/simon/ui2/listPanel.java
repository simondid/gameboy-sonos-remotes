package com.simon.ui2;

import com.simon.keyevent;

import javax.swing.*;
import java.awt.*;

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
        list.addKeyListener(new keyevent());
        list.setListData(data);

        add(new JScrollPane(list));

        this.setBackground(Color.cyan);
    }
}
