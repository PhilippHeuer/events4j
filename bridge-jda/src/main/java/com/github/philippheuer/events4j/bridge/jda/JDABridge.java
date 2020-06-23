package com.github.philippheuer.events4j.bridge.jda;

import com.github.philippheuer.events4j.api.IEventManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class JDABridge {

    /**
     * Registers a JDA EventListener that forwards all events to the EventManager
     *
     * @param eventManager EventManager
     * @param jda JDA Instance
     */
    public static void bridgeEvents(IEventManager eventManager, JDA jda) {
        jda.addEventListener(new EventHubToJDAForwarder(eventManager));
    }

    public static class EventHubToJDAForwarder implements EventListener
    {
        private IEventManager eventManager;

        public EventHubToJDAForwarder(IEventManager eventManager) {
            this.eventManager = eventManager;
        }

        @Override
        public void onEvent(GenericEvent event)
        {
            eventManager.publish(event);
        }
    }

}
