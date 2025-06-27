package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.ClientServer.Messages.ExceptionEvent;

/**
 * The ExceptionListener interface should be implemented by any class
 * that wishes to be notified of exceptions occurring during the
 * execution of a program. This mechanism allows for centralized handling
 * of exceptions in an event-driven manner.
 *
 * The implementing class should define the behavior for responding
 * to exceptions by providing an implementation for the
 * {@code exceptionOccured} method.
 */
public interface ExceptionListener  {
    /**
     * Handles the occurrence of an exception event. This method is invoked when an exception
     * is detected, and provides details about the exception through the supplied {@code ExceptionEvent}.
     *
     * @param event the ExceptionEvent containing information about the exception that occurred.
     */
    void exceptionOccured(ExceptionEvent event);
}
