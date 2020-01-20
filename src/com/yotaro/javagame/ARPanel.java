package com.yotaro.javagame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.TimerTask;

public class ARPanel extends JPanel {

    //game phase
    private int phase = 0;
    private final int               PHASE_TITLE         = 0;
    private final int               PHASE_GAMESTART     = 1;
    private final int               PHASE_GAME          = 2;
    private final int               PHASE_CLASH         = 3;
    private final int               PHASE_GAMEOVER      = 4;

    //font
    private final Font              FONT_TEXT           = new Font("MSゴシック", Font.PLAIN, 16);
    private final Font              FONT_GAMEOVER       = new Font("MSゴシック", Font.BOLD, 24);

    //score
    private     int                 score               = 0;
    private     int                 scoreHigh           = 30000;
    private     int                 my                  = 0;

    //time
    private     int                 timeWork            = 0;

    //keyboard adapter
    private     MGKeyAdapter        mgka                = null;
    private     boolean[]           keyPressTable       = null;

    //background
    private     BufferedImage       imageBackground1    = null;
    private     BufferedImage       imageBackground2    = null;
    //asteroid
    private     BufferedImage       imageAsteroid       = null;
    //space ship
    private     BufferedImage[]     imageShips          = null;
    private     int                 direction           = 0;

    //background position
    private     int                 yBackground1        = 0;
    private     int                 yBackground2        = 0;
    //asteroid position
    private     int[]               xAsteroids          = new int[100];
    private     int[]               yAsteroids          = new int[100];
    //space ship position
    private     int                 x                   = 0;
    private     int                 y                   = 0;

    //asteroid speed
    private     int[]               mxAsteroids         = new int[100];
    private     int[]               myAsteroids         = new int[100];

    //space ship speed
    private     int                 mx                  = 0;

    //asteroid size
    private     int[]               widthAsteroids      = new int[100];

    //asteroid angle
    private     int[]               angleAsteroids      = new int[100];
    private     int[]               velocityAsteroids   = new int[100];

    //timer
    private     java.util.Timer     timerThis           = null;

    //playing time
    private     int                 time                = 0;

    public ARPanel(){

        super();

        try{
            //panel size
            super.setPreferredSize(new Dimension(800, 800));
            //layout
            super.setLayout(null);

            //create key press condition list
            keyPressTable = new boolean[256];

            //load space ship image - straight
            imageShips = new BufferedImage[3];
            InputStream is0 = this.getClass().getResourceAsStream("image/imageShip0.gif");
            imageShips[0] = ImageIO.read(is0);
            is0.close();

            //load space ship image - right
            InputStream is1 = this.getClass().getResourceAsStream("image/imageShipR.gif");
            imageShips[1] = ImageIO.read(is1);
            is1.close();

            //load space ship image - left
            InputStream is2 = this.getClass().getResourceAsStream("image/imageShipL.gif");
            imageShips[2] = ImageIO.read(is2);
            is2.close();

            //load background 1
            InputStream isBackground1 = this.getClass().getResourceAsStream("image/imageBackground1.gif");
            imageBackground1 = ImageIO.read(isBackground1);
            isBackground1.close();

            //load background 2
            InputStream isBackground2 = this.getClass().getResourceAsStream("image/imageBackground2.gif");
            imageBackground2 = ImageIO.read(isBackground2);
            isBackground2.close();

            //load asteroid
            InputStream isA = this.getClass().getResourceAsStream("image/imageAsteroid.gif");
            imageAsteroid = ImageIO.read(isA);
            isA.close();


            //create key adapter and add it to panel
            mgka = new MGKeyAdapter();
            this.addKeyListener(mgka);

            //initialize
            init();

            //create timer and start
            timerThis = new java.util.Timer();
            timerThis.scheduleAtFixedRate(new TimerActionListener(), 1000l, 8l);

            //start game
            start();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR: " + e.toString());
        }
    }//end of ARPanel

    public void init(){

        time = 0;

        score = 0;

        x = 384;
        y = 640;

        mx = 0;
        my = 0;

        //init asteroid (cant see)
        for(int i = 0; i<100; i++){
            yAsteroids[i] = -9999;
        }
    }//end init

    public void start(){

        phase = PHASE_GAME;
    }//end start

    public void run(){

        time++;

        //background 1 move frequency
        if(time % 5 == 0){
            yBackground1++;
            if(yBackground1 > 0){
                yBackground1 = -800;
            }
        }

        //background2 move frequency
        if(time % 2 == 0){
            yBackground2 = yBackground2 + 1;
            if(yBackground2 > 0){
                yBackground2 = -800;
            }
        }

        //ship move frequency
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

            if(phase == PHASE_GAME){
                score = score + 10;

                //update the highest score recorded
                if(score > scoreHigh){
                    scoreHigh = score;
                }

                x = x + mx;

                //at left most
                if(x < 0){
                    x = 0;
                    mx = 0;

                //at right most
                }else if(x > this.getWidth() - 32){
                    x = this.getWidth() - 32;
                    mx = 0;
                }

            }else if(phase == PHASE_CLASH){
                timeWork++;

                x = x + mx;
                y = y + my;

                //wait for 2 seconds
                if(timeWork == 120){
                    timeWork = 0;
                    phase = PHASE_GAMEOVER;
                }

            }else if(phase == PHASE_GAMEOVER){
                timeWork++;

                //wait for 5 seconds and restart the game
                if (timeWork == 300){
                    init();

                    start();
                }
            }


            //create asteroid
            if(Math.random() < 0.02){
                int pos = -1;
                for(int i = 0; i < 100; i++){
                    if(yAsteroids[i] == -9999){
                        pos = i;
                        break;
                    }
                }

                //initialize asteroid's condition
                widthAsteroids[pos] = (int)(Math.random() * 150) + 30;
                xAsteroids[pos] = (int)(Math.random() * 800);
                yAsteroids[pos] = -widthAsteroids[pos];
                mxAsteroids[pos] = 3 - (int)(Math.random() * 6);
                myAsteroids[pos] = 5 - (int)(Math.random() * 5);
                angleAsteroids[pos] = (int)(Math.random() * 300);
                velocityAsteroids[pos] = (int)(Math.random() * 10);
            }

            //move asteroid
            for(int i = 0; i < 100; i++){
                if(yAsteroids[i] != -9999){
                    //move down
                    xAsteroids[i] = xAsteroids[i] + mxAsteroids[i];
                    yAsteroids[i] = yAsteroids[i] + myAsteroids[i];
                    angleAsteroids[i] = angleAsteroids[i] + velocityAsteroids[i];

                    //hit judgement
                    if(xAsteroids[i] < x + 10 && x + 32 - 10 < xAsteroids[i] + widthAsteroids[i]
                    && yAsteroids[i] < y + 20 && y + 40 - 20 < yAsteroids[i] + widthAsteroids[i]){
                        mx = mxAsteroids[i];
                        my = myAsteroids[i];

                        timeWork = 0;

                        phase = PHASE_CLASH;
                    }

                    //once its outside
                    if(yAsteroids[i] > 800){
                        yAsteroids[i] = -9999;
                    }
                }
            }

        }

        if(time % 2 == 1){
            repaint();
        }
    }//end run

    public void paint(Graphics g){

        //get Graphics2D
        Graphics2D g2 = (Graphics2D) g;

        //get Transform
        AffineTransform af = new AffineTransform();

        //background1
        g.drawImage(imageBackground1, 0, yBackground1, 800, 800 , this);
        g.drawImage(imageBackground1, 0, yBackground1 + 800, 800, 800 , this);

        //background2
        g.drawImage(imageBackground2, 0, yBackground2, 800, 800 , this);
        g.drawImage(imageBackground2, 0, yBackground2 + 800, 800, 800 , this);

        for (int i = 0; i < 100; i++){

            if(yAsteroids[i] != -9999){

                //rotation
                double radAsteroid = angleAsteroids[i] * Math.PI/180;
                //calculate center of mass position
                double xAsteroid = (double)xAsteroids[i] + widthAsteroids[i] / 2;
                double yAsteroid = (double)yAsteroids[i] + widthAsteroids[i] / 2;

                //add rotation and define position
                af.setToRotation(radAsteroid, xAsteroid, yAsteroid);

                //set transform
                g2.setTransform(af);

                //draw asteroid
                g2.drawImage(imageAsteroid, xAsteroids[i], yAsteroids[i], widthAsteroids[i], widthAsteroids[i], this);
            }
        }

        //change back the rotation and position
        af.setToRotation(0d, 0d, 0d);

        //set transform
        g2.setTransform(af);

        //draw space ship
        g2.drawImage(imageShips[direction], x, y, 32, 40, this);

        //draw scoring
        g2.setColor(Color.lightGray);
        g2.setFont(FONT_TEXT);
        g2.drawString("SCORE " + score, 100, 20);
        g2.drawString("HIGH-SCORE" + scoreHigh, 500, 20);

        //game over
        if(phase == PHASE_GAMEOVER){
            g2.setFont(FONT_GAMEOVER);
            g2.drawString("G A M E   O V E R", 300, 500);
        }
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



