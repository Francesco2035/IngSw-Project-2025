package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.Controller.Listeners.ControllerHourGlassListener;
import org.example.galaxy_trucker.ClientServer.Messages.HourglassEvent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Hourglass{

    private int lv;
    private int time;
    private int usages;
    private boolean startable;
    private ArrayList<ControllerHourGlassListener> listeners;
    private Timer hourglass;


    public Hourglass(){}

    public Hourglass(int lv) {
        time = 90000;
        startable = true;
        if(lv == 2) usages = 3;
        else usages = -1;
        this.listeners = new ArrayList<>();
    }


    public synchronized void startHourglass() {
        System.out.println("trying to start hourglass");
        hourglass = new Timer();
        for (ControllerHourGlassListener listener : listeners){
            listener.hourglassUpdate(new HourglassEvent("Hourglass Started: "+usages, true));
        }
        hourglass.schedule(new TimerTask(){
            @Override
            public void run() {
                usages--;
                System.out.println("Hourglass ended: " + usages + " usages left");
                if(usages>0){
                    startable = true;
                    hourglass.cancel();
                    for (ControllerHourGlassListener listener : listeners){
                        listener.hourglassUpdate(new HourglassEvent("Hourglass ended: "+usages, false));
                    }
                }
                else {
                    for (ControllerHourGlassListener listener : listeners){
                        System.out.println("Calling Hourglass listener");
                        listener.onFinish();
                    }
                }

            }
        }, time);

    }


    public synchronized void stopHourglass() {
        if(hourglass != null && !startable && usages > 0){
            hourglass.cancel();
        }
    }


    public void setListener(ControllerHourGlassListener listener){
        listeners.add(listener);
    }

    public int getUsages() {return usages;}

    public boolean isStartable() {return startable;}

    public void setLock(){startable = false;}

    public void setUsages(int usages) {this.usages = usages;}

}