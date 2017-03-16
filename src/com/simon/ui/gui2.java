package com.simon.ui;

import com.simon.Main;
import com.simon.keyevent;

import javax.swing.*;
import javax.swing.plaf.ActionMapUIResource;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by simon on 3/6/2017.
 */
public class gui2 extends JFrame implements ActionListener{
    private GraphicsDevice vc;

   public static JList list;


    public gui2 (boolean fullscreen)throws HeadlessException {

        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        vc = e.getDefaultScreenDevice();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        //   this.setTitle("sonos app");
        this.setSize(320,240);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        ArrayList<String> itemlist = new ArrayList<>();

        for(int i = 0; i< Main.activeList.size(); i++){
            itemlist.add(Main.activeList.get(i).title);
        }
        GridLayout layout = new GridLayout(2,1);
        list = new JList(itemlist.toArray());

        list.getSelectedIndex();

        JLabel label = new JLabel("davs");
        label.setFocusable(false);
        label.setSize(this.getWidth(),10);
       // label.set
        layout.addLayoutComponent("label",label);
        layout.addLayoutComponent(null,list);
        //this.setLayout(layout);
       // this.add(label);
        this.add(new JScrollPane(list));


        if(fullscreen) {
            setFullScreen(this);
        }
        this.setVisible(true);


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

    public void setFullScreen(JFrame f) {

        f.setUndecorated(true);
        f.setResizable(false);
        vc.setFullScreenWindow(f);



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Check")) {

            int index = list.getSelectedIndex();

            System.out.println("Index Selected: " + index);

            String s = (String) list.getSelectedValue();

            System.out.println("Value Selected: " + s);

        }

    }

    public static void newList1(String[] strings) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                list.setListData(strings);
                list.repaint();
            }
        });

    }
}
