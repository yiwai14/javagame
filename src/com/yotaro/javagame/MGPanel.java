package com.yotaro.javagame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

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
    private int m = 0;

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

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR: "+e.toString());
        }


    }

    public void paint(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 600);

        g.drawImage(imageMs[m], mx, my, 100, 88, this);
        g.drawImage(imagePHs[ph], px, py, 100, 100, this);
    }

    private class MGMouseAdapter extends MouseAdapter {

        public void mousePressed(MouseEvent me){
            ph = 1;
            px = me.getX() - 100;
            py = me.getY() - 100;

            if(px > mx - 50 && px < mx + 90 && py > my - 70 && py < my + 60){
                m = 1;
            }else{
                m = 0;
            }
            repaint();
        }

        public void mouseReleased(MouseEvent me){
            ph = 0;
            px = me.getX() - 100;
            py = me.getY() - 100;
            m = 0;
            repaint();
        }

        public void mouseMoved(MouseEvent me){
            px = me.getX() - 100;
            py = me.getY() - 100;
            repaint();
        }

        public void mouseDragged(MouseEvent me){
            px = me.getX() - 100;
            py = me.getY() - 100;
            repaint();
        }
    }


}
