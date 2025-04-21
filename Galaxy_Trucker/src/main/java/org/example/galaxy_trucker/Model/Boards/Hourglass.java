package org.example.galaxy_trucker.Model.Boards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.example.galaxy_trucker.Controller.HourGlassListener;

import java.lang.Thread;
import java.util.ArrayList;

public class Hourglass implements Runnable {

    private int lv;
    private int time;
    private int usages;
    private boolean startable;
    private ArrayList<HourGlassListener> listeners;

    public Hourglass(){}

    public Hourglass(int lv) {
        this.lv = lv;
        time = 600;
        startable = true;
        if(lv == 2) usages = 3;
        else usages = 2;
        this.listeners = new ArrayList<>();
    }

    public void setListener(HourGlassListener listener){
        listeners.add(listener);
    }


    @Override
    @JsonIgnore
    public void run() {

        startable = false;

        try {
            Thread.sleep(time);
            System.out.println("Hourglass ended: "+usages);
            usages--;

            if(usages>0) {
                startable = true;
            }
            else {
                for (HourGlassListener listener : listeners){
                    listener.onFinish();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



    }


    public int getUsages() {return usages;}

    public boolean isStartable() {return startable;}

    public void setLock(){startable = false;}

}