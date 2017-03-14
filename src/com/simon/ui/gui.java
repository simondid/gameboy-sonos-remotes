package com.simon.ui;

import sun.reflect.generics.tree.Tree;

import javax.management.modelmbean.ModelMBean;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * Created by simon on 3/6/2017.
 */
public class gui extends JFrame {
    private GraphicsDevice vc;
    private static JTree tree;
    static DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
    public gui(boolean fullscreen) throws HeadlessException {
        super();

        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        vc = e.getDefaultScreenDevice();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

     //   this.setTitle("sonos app");
        this.setSize(320,240);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create the root node

        //create the child nodes
        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");

        //add the child nodes to the root node
        root.add(vegetableNode);
        root.add(fruitNode);


        //create the tree by passing in the root node
        tree = new JTree(root);

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                int pathCount = path.getPathCount();


                for (int i = 0; i < pathCount; i++) {
                    System.out.print(path.getPathComponent(i).toString());
                        System.out.print(i);
                    if (i + 1 != pathCount) {
                        System.out.print("|");
                    }
                }
                System.out.println("");

                JTree tree = (JTree) e.getSource();
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
                        .getLastSelectedPathComponent();
                String selectedNodeName = selectedNode.toString();
                System.out.println();


            }



        });

        add(tree);




        if(fullscreen) {
            setFullScreen(this);
        }
        this.setVisible(true);
    }
    public static void clearList(){
      root.removeAllChildren();
    }
    public static void addItem(String name){
        System.out.println("adding items");
        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode(name);


        root.add(fruitNode);

        DefaultTreeModel m = (DefaultTreeModel) tree.getModel();
        m.reload();
    }

    public void setFullScreen(JFrame f) {

        f.setUndecorated(true);
        f.setResizable(false);
        vc.setFullScreenWindow(f);



    }
}
