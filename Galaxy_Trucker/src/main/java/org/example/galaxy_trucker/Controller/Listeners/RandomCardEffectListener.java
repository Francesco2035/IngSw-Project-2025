package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Controller.Messages.TileSets.LogEvent;

/**
 * Represents a listener interface for handling random card effects in the system.
 * Consumes a LogEvent object during the invocation of the effect.
 */
public interface RandomCardEffectListener {
     /**
      * Executes an effect based on the provided log event. Implementations of this method
      * should define the specific actions to be performed when the effect is triggered.
      *
      * @param event the LogEvent containing details such as effect description, coordinates,
      *              direction, and type that specify the context of the effect.
      */
     void Effect(LogEvent event) ;
}
