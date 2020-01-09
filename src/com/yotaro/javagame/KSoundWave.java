package com.yotaro.javagame;

import java.io.*;
import javax.sound.sampled.*;

public class KSoundWave {

    private Clip clip;

    public KSoundWave(Object obj, String fileName, boolean flgLoop){

        try{
            if(obj == null){
                obj = this;
            }
            InputStream is = obj.getClass().getResourceAsStream(fileName);
            AudioInputStream sound = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            AudioFormat format = sound.getFormat();
            DataLine.Info di = new DataLine.Info(Clip.class, format);
            this.clip = (Clip) AudioSystem.getLine(di);
            clip.open(sound);

        }catch(UnsupportedAudioFileException ex){
            ex.printStackTrace();
            return;
        }catch(IOException ex){
            ex.printStackTrace();
            return;
        }catch(LineUnavailableException ex){
            ex.printStackTrace();
            return;
        }

    } // end KSoundWave

    public void start(){
        clip.setFramePosition(0);
        clip.start();
    }

    public void stop(){
        clip.stop();
    }

}

