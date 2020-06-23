package com.github.philippheuer.events4j.bridge.jda;

import com.github.philippheuer.events4j.api.IEventManager;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.Event;

public class Discord4JBridge {

    /**
     * Forwards all Discord4j Events to the EventManager
     *
     * @param eventManager EventManager
     * @param discordClient Discord Client
     */
    public static void bridgeEvents(IEventManager eventManager, DiscordClient discordClient) {
        discordClient.login()
            .flatMapMany(gateway -> gateway.on(Event.class))
            .subscribe(event -> eventManager.publish(event));
    }

}
