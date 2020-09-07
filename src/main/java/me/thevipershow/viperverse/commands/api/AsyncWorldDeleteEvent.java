package me.thevipershow.viperverse.commands.api;

import java.io.File;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AsyncWorldDeleteEvent extends Event implements Cancellable {
    private boolean cancelled = false;

    /**
     * This constructor is used to explicitly declare an event as synchronous
     * or asynchronous.
     *
     * @param isAsync true indicates the event will fire asynchronously, false
     *                by default from default constructor
     */
    public AsyncWorldDeleteEvent(final File worldFolder) {
        super(true);
        this.worldFolder = worldFolder;
    }

    private final File worldFolder;

    public static HandlerList handlerList = new HandlerList();

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public final void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlerList;
    }
}
