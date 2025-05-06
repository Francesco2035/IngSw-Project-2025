package org.example.galaxy_trucker.Model.Boards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.galaxy_trucker.Controller.Listeners.HourGlassListener;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Hourglass{

    private int lv;
    private int time;
    private int usages;
    private boolean startable;
    private ArrayList<HourGlassListener> listeners;
    private Timer hourglass;

    public Hourglass(){}

    public Hourglass(int lv) {
        time = 60;
        startable = true;
        if(lv == 2) usages = 3;
        else usages = -1;
        this.listeners = new ArrayList<>();
    }


    public synchronized void  startHourglass() {

        hourglass = new Timer();

        hourglass.schedule(new TimerTask(){
            @Override
            public void run() {
                usages--;
                System.out.println("Hourglass ended: " + usages + " usages left");
                if(usages>0){
                    startable = true;
                }
                else {
                    for (HourGlassListener listener : listeners){
                        listener.onFinish();
                    }
                }

                hourglass.cancel();
            }
        }, time);

    }


    public synchronized void stopHourglass() {
        if(hourglass != null && !startable && usages > 0){
            hourglass.cancel();
        }
    }


    public void setListener(HourGlassListener listener){
        listeners.add(listener);
    }

    public int getUsages() {return usages;}

    public boolean isStartable() {return startable;}

    public void setLock(){startable = false;}

//
//    @Override
//    @JsonIgnore
//    public void run() {
//
//        startable = false;
//
//        try {
//            Thread.sleep(time);
//            usages--;
//
//            if(usages>0) {
//                startable = true;
//            }
//            else {
//                for (HourGlassListener listener : listeners){
//                    listener.onFinish();
//                }
//            }
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//
//
//    }



}