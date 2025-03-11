package org.example.galaxy_trucker;

import java.lang.Thread;

public class Hourglass {

    private int lv;
    private int time;
    private int usages;


    public  Hourglass(int lv) {
        this.lv = lv;
        time = 60000;
        if(lv == 2) usages = 3;
        else usages = 2;
    }

    public boolean StartTimer() throws InterruptedException {
        if(usages>0) {
            Thread t = new Thread(() -> {

                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            t.start();
            t.join();

            usages--;
            return true;
        }
        else throw new IllegalStateException("No hourglass usages left");
    }

}
