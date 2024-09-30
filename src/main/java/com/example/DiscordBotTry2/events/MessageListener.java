package com.example.DiscordBotTry2.events;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public abstract class MessageListener {
     private final Map<String, String> commandsMap;

     public MessageListener(){

         commandsMap = new HashMap<>();
         commandsMap.put("!tarkov", "Who plays Escape from tarkov , lets go arena");
         commandsMap.put("!test","Test3");
         commandsMap.put("!what","WHat now ");
         commandsMap.put("!orgos","Оргуса отказа кашкавала .Боряна го преби");
         commandsMap.put("!kolev","Who The hell is kolev ? ");
         commandsMap.put("!hi","Hello there Im the free bot ");
         commandsMap.put("!gabonemojedaprogramira", "This is true , he cant event deploy me ? ? what a noob ");
         commandsMap.put("!krow","Krow who ?  who is this ");
         commandsMap.put("!bye","GoodBye");
         commandsMap.put("!cool icon", "Благодаря бате");
         commandsMap.put("!arena", "@everyone Haide arena");
         commandsMap.put("!педал", "ПОКАЖЕТЕ КОЙ Е ПЕДАЛА");
         commandsMap.put("!tod","");
         commandsMap.put("!gabo" ,"He created me ");


     }


     public Mono<Void> processCommand(Message eventMessage){
         return Mono.just(eventMessage)
                 .filter(this::isNotFromBot)
                 .flatMap(this::handleMessage)
                 .then();
     }



     private boolean isNotFromBot(Message message){
         return message.getAuthor().map(user -> !user.isBot()).orElse(false);
     }




     private Mono<Void> handleMessage(Message message){
         String content = message.getContent().trim().toLowerCase();

         //Iterate over the commands map to find the matching
         for (Map.Entry<String, String> entry : commandsMap.entrySet()){
             if (entry.getKey().equals(content)){
                 return sendMessage(message, entry.getValue());

             }
         }


         // Iterate over the commands map to find the matching
         for (Map.Entry<String, String> entry : commandsMap.entrySet()){
             if (entry.getKey().equals(content)){
                 return sendMessage(message, entry.getValue());

             }

         }

         return Mono.empty();
     }



     private Mono<Void> sendMessage (Message message, String response){
         return message.getChannel()
                 .flatMap(channel -> {
                     String username = message.getAuthor().map(User::getMention).orElse("Unknown user");
                     return channel.createMessage(response + " From " +username);

                 }).then();
     }
}
