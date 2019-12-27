package com.yotaro.javagame;

import javax.swing.*;
import java.awt.*;

public class MGFrame extends JFrame{

    private MGPanel panel = null;

    public MGFrame(){
        super();

        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        super.setLayout(new BorderLayout());

        panel = new MGPanel();

        super.setContentPane(panel);

        super.setVisible(true);

        super.pack();
    }
}
