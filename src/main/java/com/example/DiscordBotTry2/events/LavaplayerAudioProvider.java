package com.example.DiscordBotTry2.events;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import discord4j.voice.AudioProvider;

import java.nio.ByteBuffer;


public class LavaplayerAudioProvider extends AudioProvider  {
    private final AudioPlayer audioPlayer;
    private ByteBuffer audioBuffer;


    public LavaplayerAudioProvider(AudioPlayer audioPlayer){
        this.audioPlayer = audioPlayer;
    }



    @Override
    public boolean provide(){
        AudioFrame audioFrame = audioPlayer.provide();

        if(audioFrame !=null){
            byte[] audioData = audioFrame.getData();
            ByteBuffer audioBuffer = ByteBuffer.wrap(audioData);
            this.audioBuffer = audioBuffer;
            return true;


        }
        return false;



        }

        private void close(){
        audioPlayer.destroy();

        }

    }

