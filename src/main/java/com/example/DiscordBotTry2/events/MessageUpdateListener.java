package com.example.DiscordBotTry2.events;

import org.springframework.stereotype.Service;
import discord4j.core.event.domain.message.MessageUpdateEvent;
import reactor.core.publisher.Mono;

@Service
public class MessageUpdateListener extends MessageListener implements EventListener<MessageUpdateEvent>{


    public MessageUpdateListener(VoiceService voiceService) {
        super(voiceService);
    }

    @Override
    public Class<MessageUpdateEvent> getEventType(){
        return MessageUpdateEvent.class;
    }


    @Override
    public Mono<Void> execute(MessageUpdateEvent event) {
        return Mono.just(event)
                .filter(MessageUpdateEvent::isContentChanged)
                .flatMap(MessageUpdateEvent::getMessage)
                .flatMap(super::processCommand);
    }
}