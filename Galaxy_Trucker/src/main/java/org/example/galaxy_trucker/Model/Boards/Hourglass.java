package org.example.galaxy_trucker.Model.Boards;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.lang.Thread;

public class Hourglass extends Thread{

    private int lv;
    private int time;
    private int usages;
    private boolean startable;


    public  Hourglass(int lv) {
        this.lv = lv;
        time = 5000;
        startable = true;
        if(lv == 2) usages = 3;
        else usages = 2;
    }

    @Override
    @JsonIgnore
    public synchronized void run() {

        startable = false;

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        usages--;

        if(usages>0) {
            startable = true;
        }

    }


    public int getUsages() {return usages;}

    public boolean isStartable() {return startable;}

    public void setLock(){startable = false;}

}