package com.example.DiscordBotTry2.events;



import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import discord4j.voice.AudioProvider;

import java.nio.ByteBuffer;

public class LavaplayerAudioProvider extends AudioProvider {
    private final AudioPlayer player;
    private final MutableAudioFrame frame = new MutableAudioFrame();


    public LavaplayerAudioProvider(final AudioPlayer player){
        // Allocate a Butebuffer  - needed for Discordj's AudioPlayer
       super(
               ByteBuffer.allocate(
                       StandardAudioDataFormats.DISCORD_OPUS.maximumChunkSize()
               )
       );
       // Set LavaPlayer's MutableAudioFrame to use the same buffer as the one we
        //just allocated

        this.player = player;
        this.frame.setBuffer(getBuffer());

    }
    @Override
    public boolean provide() {
        //AudioPlayer writes audi data to its AudioFrame
        final boolean didProvide = player.provide(frame);
        //If audio was provided flip from write-mode to read-mode
        if (didProvide)   getBuffer().flip();

        return didProvide;

    }
}
