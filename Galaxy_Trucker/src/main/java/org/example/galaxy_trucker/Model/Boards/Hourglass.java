package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.Controller.Listeners.ControllerHourGlassListener;
import org.example.galaxy_trucker.ClientServer.Messages.HourglassEvent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The Hourglass class simulates the behavior of an hourglass timer, capable of counting down
 * a defined duration, managing a fixed number of usages, and notifying listeners about
 * hourglass-related events. The class provides methods to start, stop, and control the
 * hourglass as well as manage its listeners.
 */
public class Hourglass{

    /**
     * Represents the countdown duration of the Hourglass in milliseconds.
     * This value defines the time interval for which the hourglass runs before performing actions.
     * It is utilized as the delay in tasks scheduled in the associated {@code Timer}.
     */
    private int time;
    /**
     * Tracks the number of remaining usages for the hourglass.
     *
     * This value determines how many times the hourglass can be started. A value of -1
     * indicates that there is no limit to the number of usages. Each time the hourglass
     * completes its cycle, this value is decremented. If the value reaches 0, the hourglass
     * is no longer functional.
     */
    private int usages;
    /**
     * Indicates whether the hourglass is in a state where it can be started.
     * This variable controls the startability of the hourglass by determining
     * if it is currently available to be activated.
     *
     * When {@code true}, the hourglass can be started. If {@code false},
     * the hourglass is either already running or conditions are not met
     * for it to be initiated (e.g., no remaining usages or locked state).
     */
    private boolean startable;
    /**
     * A collection of registered {@code ControllerHourGlassListener} instances that will receive
     * notifications about hourglass-related events. This list is used to maintain a group of listeners
     * that are notified when the hourglass timer starts, stops, or completes its operations.
     */
    private ArrayList<ControllerHourGlassListener> listeners;
    /**
     * Represents the Timer object used to manage the behavior of the hourglass
     * in the Hourglass class. This timer is responsible for scheduling and
     * executing the tasks associated with starting and stopping the hourglass.
     */
    private Timer hourglass;


    /**
     * Constructs a new instance of the Hourglass class.
     * This is the default no-argument constructor that initializes a basic Hourglass object.
     */
    public Hourglass(){}

    /**
     * Constructs an Hourglass object with an initial state based on the provided level.
     *
     * @param lv the level of the hourglass. If the level is 2, the hourglass has a maximum of 3 usages.
     *           For other levels, the usages are unlimited.
     */
    public Hourglass(int lv) {
        time = 90000;
        startable = true;
        if(lv == 2) usages = 3;
        else usages = -1;
        this.listeners = new ArrayList<>();
    }


    /**
     * Starts the hourglass timer and schedules its events.
     *
     * This method initializes a new timer instance and notifies all registered listeners
     * with an update event to signal that the hourglass has been started. The timer then
     * schedules a task that counts down the hourglass usages over a predefined period.
     * Once the timer task runs, it decrements the usages and performs the following actions:
     *
     * 1. If usages are remaining, the hourglass becomes startable again, and a corresponding
     *    update event is sent to all listeners to indicate the hourglass has ended but is still usable.
     * 2. If no usages are left, it triggers a finish event for all listeners, signaling the
     *    final termination of the hourglass.
     *
     * This method is synchronized to ensure thread safety for shared resources like
     * the timer and listeners.
     *
     * Note: The timer's duration and behavior depend on the `time` field of the containing class.
     *
     * Preconditions:
     * - The field `time` is expected to be appropriately initialized to define the timer's duration.
     * - Listeners should be registered prior to calling this method to ensure notification of events.
     *
     * Postconditions:
     * - The timer will be active for the specified duration of `time`, managing usages appropriately.
     * - Listeners will receive updates on the status of the hourglass, including when it starts, ends,
     *   and completes all usages.
     */
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


    /**
     * Stops the hourglass activity if specific conditions are met.
     * This method checks if the hourglass is active (hourglass is not null),
     * is currently not startable, and has remaining usages. If all these
     * conditions are true, it cancels the hourglass operation.
     *
     * This is a synchronized method to ensure thread safety when multiple
     * threads attempt to modify or stop the hourglass concurrently.
     */
    public synchronized void stopHourglass() {
        if(hourglass != null && !startable && usages > 0){
            hourglass.cancel();
        }
    }


    /**
     * Registers a new listener to receive hourglass-related events.
     * The listener will be notified of updates such as the hourglass starting or finishing.
     *
     * @param listener the {@code ControllerHourGlassListener} to be added. This listener will handle
     *                 events related to the hourglass's state and updates.
     */
    public void setListener(ControllerHourGlassListener listener){
        listeners.add(listener);
    }

    /**
     * Retrieves the current number of usages associated with the Hourglass instance.
     *
     * @return the number of usages available or consumed for the Hourglass.
     */
    public int getUsages() {return usages;}

    /**
     * Determines whether the hourglass can be started.
     *
     * @return true if the hourglass is in a startable state, false otherwise.
     */
    public boolean isStartable() {return startable;}

    /**
     * Locks the hourglass, preventing it from being started.
     * This method sets the internal state to indicate that the hourglass cannot be started.
     */
    public void setLock(){startable = false;}

    /**
     * Sets the number of usages for the Hourglass.
     *
     * @param usages the number of uses to be set for the Hourglass
     */
    public void setUsages(int usages) {this.usages = usages;}

}