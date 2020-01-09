package com.yotaro.javagame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.TimerTask;

public class MGPanel extends JPanel {

    private MGMouseAdapter mgma = null;

    //hammer position
    private int px = 0;
    private int py = 0;

    //mole position
    private int mx = 0;
    private int my = 0;

    //hammer image
    private BufferedImage[] imagePHs = null;
    private int ph = 0;

    //mole image
    private BufferedImage[] imageMs = null;
    //0 = alight
    //1 = hit
    private int m = 0;

    //mole time
    private int timeM = 0;

    //timer
    private java.util.Timer timerThis = null;

    //0 = gaming phase
    //1 = game over
    private int phase = 0;

    private BufferedImage imageBackground = null;

    //score
    private int score = 0;
    private Font fontScore = new Font("MS ゴシック", Font.BOLD | Font.ITALIC,24);

    //time limit
    private int time = 1859;
    private Font fontTime = new Font("MS ゴシック", Font.BOLD, 24);

    //game over
    private Font fontGameOver = new Font("MS ゴシック", Font.BOLD, 48);

    //BGM
    private KSoundMidi midiMoleDance = null;

    //Sound effect
    private KSoundWave waveMoleAppear = null;
    private KSoundWave waveHammerMiss = null;
    private KSoundWave waveHammerHit = null;

    public MGPanel(){
        super();

        try{
            super.setPreferredSize(new Dimension(800, 600));
            super.setLayout(null);
            mgma = new MGMouseAdapter();
            super.addMouseListener(mgma);
            super.addMouseMotionListener(mgma);

            //hammer
            imagePHs = new BufferedImage[2];
            InputStream isPH00 = this.getClass().getResourceAsStream("PH00.gif");
            imagePHs[0] = ImageIO.read(isPH00);
            isPH00.close();

            //hammer HIT
            InputStream isPH01 = this.getClass().getResourceAsStream("PH01.gif");
            imagePHs[1] = ImageIO.read(isPH01);
            isPH01.close();

            //mole
            imageMs = new BufferedImage[2];
            InputStream isML00 = this.getClass().getResourceAsStream("M00.gif");
            imageMs[0] = ImageIO.read(isML00);
            isML00.close();

            //mole hit
            InputStream isML01 = this.getClass().getResourceAsStream("M01.gif");
            imageMs[1] = ImageIO.read(isML01);
            isML01.close();

            //load background
            InputStream isBackground = this.getClass().getResourceAsStream("Background.jpg");
            imageBackground = ImageIO.read(isBackground);
            isBackground.close();

            //create BGM
            midiMoleDance = new KSoundMidi(this, "sound/MoguraDance.mid", false);

            //create sound effect
            waveMoleAppear = new KSoundWave(this, "sound/MoguraDeru.wav", false);
            waveHammerMiss = new KSoundWave(this, "sound/PicoHammerMiss.wav", false);
            waveHammerHit = new KSoundWave(this, "sound/PicoHammerHit.wav", false);

            //initialise
            init();

            //timer
            timerThis = new java.util.Timer();
            timerThis.scheduleAtFixedRate(new TimerActionTimerTask(), 1000l, 16l);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR: "+e.toString());
        }
    }//end MGPanel

    public void init(){

        //change to gaming phase
        phase = 0;
        //mole condition
        m = 0;
        score = 0;
        time = 1859;

        //BGM initialise and start
        midiMoleDance.init();
        midiMoleDance.start();
    }//end init

    private class TimerActionTimerTask extends TimerTask{
        public void run(){
            MGPanel.this.run();
            repaint();
        }
    }//end TimerActionTimerTask

    //executing method
    public void run(){

        switch(phase){
            case 0:
                time--;
                //time is up
                if(time == 0){
                    time = 300;
                    phase = 1;
                    break;
                }
                //mole been hit
                if(timeM != 0){
                    timeM--;
                    if(timeM == 0){
                        m = 0;
                        mx = (int)(Math.random() *550);
                        my = (int)(Math.random() *450);
                        waveMoleAppear.start();
                    }
                }
                break;
            case 1:
                time--;
                if(time == 0){
                    init();
                }
                break;
        }//end switch
    }//end run

    public void paint(Graphics g){

        //background
        g.drawImage(imageBackground, 0, 0, 800, 600, this);
        //mole
        g.drawImage(imageMs[m], mx, my, 100, 88, this);
        //hammer
        g.drawImage(imagePHs[ph], px, py, 100, 100, this);

        //score
        g.setColor(Color.yellow);
        g.setFont(fontScore);
        g.drawString("Score: " + score, 0, 24);

        if(phase == 0){
            g.setColor(Color.red);
            g.setFont(fontTime);
            g.drawString("Time remeining: " + time/60, 300, 24);
        }else if(phase == 1){
            g.setColor(Color.lightGray);
            g.setFont(fontGameOver);
            g.drawString("Game over", 240, 300);
            if(score >= 30){
                g.drawString("Good Job!", 240, 350);
            }else if(score >= 20){
                g.drawString("That was ok.", 240, 350);
            }else if(score >= 10){
                g.drawString("Umm...", 240, 350);
            }else{
                g.drawString("You Suck", 240, 350);
            }
        }
    }//end paint

    private class MGMouseAdapter extends MouseAdapter {

        public void mousePressed(MouseEvent me){

            //gaming phase
            if(phase == 0){
                ph = 1;
                px = me.getX() - 100;
                py = me.getY() - 100;
                if(m == 0){
                    if(px > mx - 50 && px < mx + 90 && py > my - 70 && py < my + 60){
                        m = 1;
                        timeM = 30;
                        score++;
                        waveHammerHit.start();
                    }else{
                        waveHammerMiss.start();
                    }
                }
            }
        }//end mousePressed

        public void mouseReleased(MouseEvent me){
            ph = 0;
            px = me.getX() - 100;
            py = me.getY() - 100;
            m = 0;
        }

        public void mouseMoved(MouseEvent me){
            px = me.getX() - 100;
            py = me.getY() - 100;
        }

        public void mouseDragged(MouseEvent me){
            px = me.getX() - 100;
            py = me.getY() - 100;
        }
    }//end MGMouseAdapter


}
