package com.simon.ui2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created by simon on 3/14/2017.
 */
public class loaderPanel extends JPanel {
    public JLabel loaderPanel() {
        BufferedImage wPic = null;  URL yrl =this.getClass().getResource("p3.gif");
        try {
            wPic = ImageIO.read(this.getClass().getResource("p3.gif"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel wIcon = new JLabel(new ImageIcon(yrl));
        return wIcon;
    }
}