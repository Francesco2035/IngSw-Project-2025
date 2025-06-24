package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Controller.Messages.HourglassEvent;

public interface ControllerHourGlassListener {

    public void onFinish();

    public void hourglassUpdate(HourglassEvent event);
}
