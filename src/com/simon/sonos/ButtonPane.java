package com.simon.sonos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by simon on 3/9/2017.
 */
public class ButtonPane extends JPanel  {

    private JButton[][] buttons;

    public ButtonPane(JFrame mainFram, String text) {

        super(new GridLayout(1, 3));
        int row = 1;
        int col = 3;

        String[] names = {"ADD","Request","Cancel"};

        buttons = new JButton[row][col];
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                final int curRow = i;
                final int curCol = j;
                buttons[i][j] = new JButton(names[j]);


                buttons[i][j].addKeyListener(enter);
                buttons[i][j].addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_UP:
                                if (curRow > 0)
                                    buttons[curRow - 1][curCol].requestFocus();
                                break;
                            case KeyEvent.VK_DOWN:
                                if (curRow < buttons.length - 1)
                                    buttons[curRow + 1][curCol].requestFocus();
                                break;
                            case KeyEvent.VK_LEFT:
                                if (curCol > 0)
                                    buttons[curRow][curCol - 1].requestFocus();
                                break;
                            case KeyEvent.VK_RIGHT:
                                if (curCol < buttons[curRow].length - 1)
                                    buttons[curRow][curCol + 1].requestFocus();
                                break;
                            default:
                                break;
                        }
                    }
                });
                add(buttons[i][j]);
            }
        }

        JFrame f = new JFrame();



        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout(2,1));
        JTextField te = new JTextField("hej");
        te.setEditable(false);

        te.setFocusable(false);
        f.add(te);
        f.add(this);
        f.setUndecorated(true);
        f.setResizable(false);
        f.pack();
        f.setLocation(mainFram.getLocation().x+mainFram.getSize().width/2-f.getSize().width/2,mainFram.getLocation().y+mainFram.getSize().height/2-f.getSize().height/2);

        f.setVisible(true);


    }

    private KeyListener enter = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                ((JButton) e.getComponent()).doClick();

            }
        }
    };
}