package com.yotaro.javagame;

import javax.swing.*;
import java.awt.*;

public class HelloJavaGame {

    public HelloJavaGame() {
        //フレーム生成&命名
        JFrame frame = new JFrame("Hello Java Game!!");

        //press x and exit, otherwise フレームは消えるがterminalに戻ってこなくなる
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 枠設定
        frame.setLayout(new BorderLayout());

        //中身の白いパネル生成
        JPanel panel = new JPanel();

        //サイズ設定
        panel.setPreferredSize(new Dimension(800, 600));
        //
        panel.setLayout(null);

        //パネルをフレームに乗せる
        frame.setContentPane(panel);


        //
        JLabel label1 = new JLabel("Hello Java Game!!");

        //
        label1.setFont(new Font("MS ゴシック", Font.BOLD, 24));

        //
        panel.add(label1);

        // x, y, width, height
        label1.setBounds(100, 100, 300, 30);


        //
        JLabel label2 = new JLabel("v(^_^)");

        //
        label2.setFont(new Font("MS ゴシック", Font.BOLD, 48));

        //
        panel.add(label2);

        // color RGB
        label2.setForeground(new Color(255, 96, 0));

        //
        label2.setBounds(180, 180, 300, 50);


        // 表示
        frame.setVisible(true);
        // サイズ最適化
        frame.pack();
    }
}
