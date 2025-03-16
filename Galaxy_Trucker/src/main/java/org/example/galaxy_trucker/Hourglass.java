package org.example.galaxy_trucker;

import java.lang.Thread;

public class Hourglass {

    private int lv;
    private int time;
    private int usages;
    private boolean Startable;


    public  Hourglass(int lv) {
        this.lv = lv;
        time = 5000;
        Startable = true;
        if(lv == 2) usages = 3;
        else usages = 2;
    }

    public synchronized void StartTimer() throws InterruptedException {
        if(usages>0) {
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(time);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });

            t.start();

            usages--;

        }
        else throw new IllegalStateException("No hourglass usages left");

    }


    public int getUsages() {return usages;}

}
