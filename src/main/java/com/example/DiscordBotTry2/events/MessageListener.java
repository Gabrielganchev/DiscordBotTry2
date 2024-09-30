package com.example.DiscordBotTry2.events;

import discord4j.core.object.entity.channel.AudioChannel;
import discord4j.core.object.VoiceState;


import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public abstract class MessageListener {
        private final Map<String, String> commandsMap;
        private final VoiceService voiceService;//Inject Voiceservice



     public MessageListener(VoiceService voiceService){
         this.voiceService=voiceService;//Initialize the voiceService

         commandsMap = new HashMap<>();
         commandsMap.put("!tarkov", "Who plays Escape from tarkov , lets go arena");
         commandsMap.put("!test","Test3");
         commandsMap.put("!what","WHat now ");
         commandsMap.put("!orgos","Оргуса отказа кашкавала .Боряна го преби");
         commandsMap.put("!kolev","Who The hell is kolev ? ");
         commandsMap.put("!hi","Hello there Im the free bot ");
         commandsMap.put("!gabonemojedaprogramira", "This is true , he cant event deploy me ? ? what a noob ");
         commandsMap.put("!krow","Nedqlko  ");
         commandsMap.put("!bye","GoodBye");
         commandsMap.put("!cool icon", "Благодаря бате");
         commandsMap.put("!arena", "@everyone Haide arena");
         commandsMap.put("!педал", "ПОКАЖЕТЕ КОЙ Е ПЕДАЛА");
         commandsMap.put("!tod","");
         commandsMap.put("!gabo" ,"He created me ");
         commandsMap.put("!Voice","I dont have a voice");
         commandsMap.put("!пидал","Ще кажа на Боряна,  Оргусе!!!");


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
         Member member =message.getAuthorAsMember().block();
         Guild guild = member.getGuild().block();

         if (content.startsWith("!play")){
             String trackUrl = content.substring("!play".length());
             return handlePlayCommand(guild,member,trackUrl);

         }else if(content.equals("!stop")){
             return handleStopCommand();
         }




         // Iterate over the commands map to find the matching
         for (Map.Entry<String, String> entry : commandsMap.entrySet()){
             if (entry.getKey().equals(content)){
                 return sendMessage(message, entry.getValue());

             }

         }

         return Mono.empty();
     }


    /*private Mono<Void> handlePlayCommand(Guild guild, Member member, String trackUrl) {
        // Check if the member is in a voice channel
        return member.getVoiceState()
                .flatMap(voiceState -> {
                    // Ensure the voice state is present and the member is in a channel
                    if (voiceState != null && voiceState.getChannelId().isPresent()) {
                        // Join the voice channel
                        return guild.getVoiceChannelById(voiceState.getChannelId().get())
                                .flatMap(channel -> channel.join(spec -> {
                                    // Set the audio provider here
                                    spec.setProvider(voiceService.getAudioProvider());
                                }))
                                .then(loadAndPlay(trackUrl)); // Load and play the track
                    } else {
                        // Member is not in a voice channel
                        return sendMessage(member.getId().asString(), "You need to be in a voice channel to play music.");
                    }
                })
                .onErrorResume(throwable -> {
                    // Handle any errors (like not being able to join the voice channel)
                    return sendMessage(member.getId().asString(), "Failed to join voice channel: " + throwable.getMessage());
                });
    }*/



    private Mono<Void> handlePlayCommand(Guild guild, Member member, String trackUrl) {
        String locaTrackPath = "/home/gabriel/Desktop/DiscordBotTry2/Sounds/grenade-plonk-sound-effect-tarkov-louder.mp3";//path

        return voiceService.connectToVoiceChannel(guild, member)
                .then(voiceService.loadAndPlay(locaTrackPath))
                .onErrorResume(e -> {
                    System.err.println("Error while trying to play music: " + e.getMessage());
                    return Mono.empty();
                });
    }
















    /*private Mono<Void> handlePlayCommand(Guild guild, Member member, String trackUrl) {
        return member.getVoiceState()
                .flatMap(voiceState -> voiceState.getChannel())
                .flatMap(channel -> {
                    // Create the LavaplayerAudioProvider using the audio player from the VoiceService
                    LavaplayerAudioProvider provider = new LavaplayerAudioProvider(voiceService.getAudioPlayer());

                    // Join the voice channel and set the audio provider
                    return channel.join(spec -> {
                        spec.setProvider(provider); // Set the audio provider
                    });
                })
                .then(Mono.defer(() -> {
                    // Load and play the track after successfully joining the channel
                    voiceService.loadAndPlay(trackUrl);
                    return Mono.empty();
                }))
                .switchIfEmpty(Mono.error(new RuntimeException("You need to be in a voice channel to play music."))).then();
    }*/











    private Mono<Void> handleStopCommand(){
         return voiceService.disconnectFromVoiceChannel();
     }



     private Mono<Void> sendMessage (Message message, String response){
         return message.getChannel()
                 .flatMap(channel -> {
                     String username = message.getAuthor().map(User::getMention).orElse("Unknown user");
                     return channel.createMessage(response + " From " +username);

                 }).then();
     }
}
