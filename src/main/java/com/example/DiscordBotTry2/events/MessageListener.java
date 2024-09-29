package com.example.DiscordBotTry2.events;

import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Component
public abstract class MessageListener {

    public Mono<Void> processCommand(Message eventMessage) {
        return Mono.just(eventMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!tarkov"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Orgusa se nasra batko"))
                .then();
    }
}