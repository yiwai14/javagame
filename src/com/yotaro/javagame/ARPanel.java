package com.yotaro.javagame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.TimerTask;

public class ARPanel extends JPanel {

    private MGKeyAdapter mgka = null;
    private boolean[] keyPressTable = null;

    //space ship position
    private int x = 0;
    private int y = 0;

    //velocity
    private int mx = 0;

    //space ship
    private BufferedImage[] imageShips = null;
    private int direction = 0;

    //timer
    private java.util.Timer timerThis = null;

    //time
    private int time = 0;

    public ARPanel(){
        super();

        try{
            //panel size
            super.setPreferredSize(new Dimension(800, 800));
            //layout
            super.setLayout(null);

            //create key press condition list
            keyPressTable = new boolean[256];

            //load space ship image
            imageShips = new BufferedImage[3];
            InputStream is0 = this.getClass().getResourceAsStream("image/imageShip0.gif");
            imageShips[0] = ImageIO.read(is0);
            is0.close();

            InputStream is1 = this.getClass().getResourceAsStream("image/imageShip1.gif");
            imageShips[0] = ImageIO.read(is1);
            is1.close();

            InputStream is2 = this.getClass().getResourceAsStream("image/imageShip2.gif");
            imageShips[0] = ImageIO.read(is2);
            is2.close();

            //create key adapter and add it to panel
            mgka = new MGKeyAdapter();
            this.addKeyListener(mgka);

            //initialize
            init();

            //create timer and start
            timerThis = new java.util.Timer();
            timerThis.scheduleAtFixedRate(new TimerActionListener(), 1000l, 8l);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR: " + e.toString());
        }
    }//end of ARPanel

    public void init(){
        time = 0;
        x = 384;
        y = 640;
    }//end init

    public void run(){
        time++;

        //ship move timing
        if(time % 6 == 0){
            direction = 0;

            //left
            if(isKeyCodePressed(KeyEvent.VK_LEFT)){
                direction = 2;
                mx = mx - 1;
            }
            if (isKeyCodePressed(KeyEvent.VK_RIGHT)){
                direction = 1;
                mx = mx + 1;
            }
        }

        //move timing
        if(time % 2 == 0){
            x = x + mx;

            //at the left most
            if(x<0){
                x = 0;
                mx = 0;
            //at the right most
            }else if (x > this.getWidth() - 32){
                x = this.getWidth() - 32;
                mx = 0;
            }
        }else{
            repaint();
        }
    }//end run

    public void paint(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 800);
        g.drawImage(imageShips[direction], x, y, 32, 40, this);
    }//end paint

    public boolean isKeyCodePressed(int keyCode){
        return keyPressTable[keyCode];
    }//end iskeyCodePressed

    private class MGKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent ke){
            int code = ke.getKeyCode();
            if(code < 256){
                keyPressTable[code] = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent ke) {
            int code = ke.getKeyCode();
            if(code < 256){
                keyPressTable[code] = false;
            }
        }
    }//end MGKeyAdapter

    private class TimerActionListener extends TimerTask{

        @Override
        public void run() {
            ARPanel.this.run();
        }//end run

    }//end TimerActionListener
}



