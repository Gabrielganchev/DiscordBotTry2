package com.example.DiscordBotTry2.events;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.AudioChannel;
import discord4j.voice.VoiceConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class VoiceService {

    private final AudioPlayerManager audioPlayerManager;
    private final AudioPlayer audioPlayer;
    private VoiceConnection voiceConnection;



    @Autowired
    public VoiceService(AudioPlayerManager audioPlayerManager){

        this.audioPlayerManager = audioPlayerManager;
        this.audioPlayer = audioPlayerManager.createPlayer();


    }

    public Mono<Void> connectToVoiceChannel(Guild guild,Member member) {
        AudioChannel audioChannel = member.getVoiceState()
                .flatMap(state -> state.getChannel()).block();
        return audioChannel.join(spec-> spec.setProvider(new LavaplayerAudioProvider(audioPlayer)))
                .doOnNext(vc -> this.voiceConnection = vc).then();



    }








    public void loadAndPlay(String trackUrl){
        audioPlayerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                // need logic to play the track

                audioPlayer.playTrack(track);



            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                List<AudioTrack> tracks = playlist.getSelectedTrack() != null ?
                        List.of(playlist.getSelectedTrack()) : playlist.getTracks();
                if (!tracks.isEmpty()){
                    audioPlayer.playTrack(tracks.get(0));//Play the first track
                }

                //logic to handle the playlist
            }

            @Override
            public void noMatches() {
                //handle no matches found
                System.out.println("No matches found");

            }

            @Override
            public void loadFailed(FriendlyException e) {
                //handle the loading failure logic


                System.err.println("Track loading failed "  + e.getMessage());


            }
        });
    }


    public Mono<Void> disconnectFromVoiceChannel(){
        if (voiceConnection != null){
            return voiceConnection.disconnect()
                    .doOnTerminate(() -> {
                audioPlayer.stopTrack();
                audioPlayer.destroy();
            });

        }
        return Mono.empty();
    }



    }
