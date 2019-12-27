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
    private String message = null;
    private int x = 0;
    private int y = 0;

    private BufferedImage[] imagePHs = null;
    private int ph = 0;

    public MGPanel(){
        super();

        try{
            super.setPreferredSize(new Dimension(800, 600));
            super.setLayout(null);
            mgma = new MGMouseAdapter();
            super.addMouseListener(mgma);
            super.addMouseMotionListener(mgma);

            //ピコピコハンマー
            imagePHs = new BufferedImage[2];
            InputStream is0 = this.getClass().getResourceAsStream("PHOO.gif");
            imagePHs[0] = ImageIO.read(is0);
            is0.close();
            //ピコピコハンマー HIT
            InputStream is1 = this.getClass().getResourceAsStream("PH01.gif");
            imagePHs[1] = ImageIO.read(is1);
            is1.close();

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR: "+e.toString());
        }


    }

    public void paint(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 600);

        g.drawImage(imagePHs[ph], x, y, 100, 100, this);

        /*
        if(message != null){
            g.setColor(Color.yellow);
            g.drawString(message, x, y);
        }
         */
    }

    private class MGMouseAdapter extends MouseAdapter {

        public void mousePressed(MouseEvent me){
            //message = "Mouse pressed";
            //System.out.println(me);
            ph = 1;
            x = me.getX();
            y = me.getY();
            repaint();
        }

        public void mouseReleased(MouseEvent me){
            //message = "Mouse released";
            //System.out.println(me);
            ph = 0;
            x = me.getX();
            y = me.getY();
            repaint();
        }

        /*
        public void mouseClicked(MouseEvent me){
            message = "Mouse clicked";
            System.out.println(me);
            x = me.getX();
            y = me.getY();
            repaint();
        }
         */

        public void mouseMoved(MouseEvent me){
            //message = "Mouse moved";
            //System.out.println(me);
            x = me.getX();
            y = me.getY();
            repaint();
        }

        public void mouseDragged(MouseEvent me){
            //message = "Mouse dragged";
            //System.out.println(me);
            x = me.getX();
            y = me.getY();
            repaint();
        }
    }


}
