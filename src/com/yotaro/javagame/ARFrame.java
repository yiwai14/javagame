package com.yotaro.javagame;

import javax.swing.*;
import java.awt.*;

public class ARFrame extends JFrame {
    private ARPanel panel = null;

    public ARFrame(){
        super();

        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        super.setLayout(new BorderLayout());

        panel = new ARPanel();

        super.setContentPane(panel);

        super.setVisible(true);

        super.pack();
    }
}
