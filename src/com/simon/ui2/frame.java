package com.simon.ui2;


import javax.swing.*;
import javax.swing.plaf.ActionMapUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static com.simon.ui.gui2.list;

/**
 * Created by simon on 3/14/2017.
 */
public class frame extends JFrame {
    private GraphicsDevice vc;
    public InfoPanel Infopanel;
    public static listPanel listPanel;
    GraphicsEnvironment e;



    public frame(boolean fullscreen) throws HeadlessException {
      //  super("title");




        setLayout(new BorderLayout());

        e = GraphicsEnvironment.getLocalGraphicsEnvironment();

        vc = e.getDefaultScreenDevice();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        //   this.setTitle("sonos app");
      // this.setSize(320,240);
        this.setPreferredSize(new Dimension(320,240));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        Container c = getContentPane();



        Infopanel = new InfoPanel();

        listPanel = new listPanel();
        c.add(listPanel);
        c.add(Infopanel,BorderLayout.NORTH);
        c.add(listPanel);
        System.out.println("trigger "+fullscreen);
        if(fullscreen){
            setFullScreen(this);
        }

        this.pack();
        this.setVisible(true);
    }
    public void setFullScreen(JFrame f) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setPreferredSize(new Dimension(dim.getSize()));
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);

        f.setUndecorated(true);
        f.setResizable(false);

        GraphicsDevice device = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getScreenDevices()[0];
        device.setFullScreenWindow(f);



    }
    public static int popup(JFrame f){
        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
        configureOptionPane();
        Object[] options = {"ADD",
                "Replace",
                "Cancel"};


        Object[] op = {"ADD","Replace","Cancel"};
        int n = JOptionPane.showOptionDialog(f,
                "what to do with playlist",
                "play list handling",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[2]);




        return n;
    }
    private static void configureOptionPane() {
        if(UIManager.getLookAndFeelDefaults().get("OptionPane.actionMap") == null)
        {
            UIManager.put("OptionPane.windowBindings", new
                    Object[] {
                    "ESCAPE", "close",
                    "LEFT", "left",
                    "KP_LEFT", "left",
                    "RIGHT", "right",
                    "KP_RIGHT", "right"
            });
            ActionMap map = new ActionMapUIResource();
            map.put("close", new OptionPaneCloseAction());
            map.put("left", new OptionPaneArrowAction(false));
            map.put("right", new OptionPaneArrowAction(true));
            UIManager.getLookAndFeelDefaults().put
                    ("OptionPane.actionMap", map);
        }
    }
    private static class OptionPaneCloseAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            JOptionPane optionPane = (JOptionPane) e.getSource();
            optionPane.setValue(JOptionPane.CLOSED_OPTION);
        }
    }
    private static class OptionPaneArrowAction extends AbstractAction {
        private boolean myMoveRight;
        OptionPaneArrowAction(boolean moveRight) {
            myMoveRight = moveRight;
        }
        public void actionPerformed(ActionEvent e) {
            JOptionPane optionPane = (JOptionPane) e.getSource();
            EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();

            eq.postEvent(new KeyEvent(
                    optionPane,
                    KeyEvent.KEY_PRESSED,
                    e.getWhen(),
                    (myMoveRight) ? 0 : InputEvent.SHIFT_DOWN_MASK,
                    KeyEvent.VK_TAB,
                    KeyEvent.CHAR_UNDEFINED,
                    KeyEvent.KEY_LOCATION_UNKNOWN
            ));
        }
    }
    public static void newList(String[] strings) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                listPanel.list.setListData(strings);
                listPanel.list.repaint();
            }
        });

    }
}
