package com.example.DiscordBotTry2.events;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class MessageCreateListener extends MessageListener implements EventListener<MessageCreateEvent> {

    public MessageCreateListener(VoiceService voiceService) {
        super(voiceService);
    }

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return processCommand(event.getMessage());
    }
}