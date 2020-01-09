package com.yotaro.javagame;

import java.io.*;
import javax.sound.midi.*;

public class KSoundMidi {

    private Sequencer sequencer;

    public KSoundMidi(Object obj, String fileName, boolean flgLoop){

        try{
            if(obj == null){
                obj = this;
            }
            InputStream is = obj.getClass().getResourceAsStream(fileName);
            Sequence s = MidiSystem.getSequence(new BufferedInputStream(is));
            this.sequencer = MidiSystem.getSequencer();
            if(flgLoop){
                this.sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            }
            this.sequencer.open();
            this.sequencer.setSequence(s);

        }catch(InvalidMidiDataException ex){
            ex.printStackTrace();
            return;
        }catch(MidiUnavailableException ex){
            ex.printStackTrace();
            return;
        }catch(IOException ex){
            ex.printStackTrace();
            return;
        }

    } // end KSoundMidi


    public void start(){
        sequencer.start();
    }

    public void stop(){
        sequencer.stop();
    }

    public void init(){
        sequencer.setTickPosition(0l);
    }

    public boolean isRunning(){
        return sequencer.isRunning();
    }

}

