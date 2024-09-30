package com.example.DiscordBotTry2.events;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
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
import discord4j.core.object.VoiceState;
import reactor.core.publisher.Mono;


import java.io.File;
import java.sql.SQLOutput;
import java.util.List;

@Service
public class VoiceService {

    private final AudioPlayerManager audioPlayerManager;
    private final AudioPlayer audioPlayer;
    private VoiceConnection voiceConnection;

    @Autowired
    public VoiceService(AudioPlayerManager audioPlayerManager) {
        this.audioPlayerManager = audioPlayerManager;
        this.audioPlayer = audioPlayerManager.createPlayer();
    }

    // Returns the audio player
    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    // Connect to voice channel
    public Mono<Void> connectToVoiceChannel(Guild guild, Member member) {
        return member.getVoiceState()
                .flatMap(VoiceState::getChannel)
                .flatMap(channel -> channel.join(spec -> spec.setProvider(new LavaplayerAudioProvider(audioPlayer))))
                .doOnNext(vc -> this.voiceConnection = vc)
                .then();
    }

    // Load and play a track
    public Mono<Void> loadAndPlay(String trackUrl) {

        return Mono.create(sink -> {
            String localPath = "/home/gabriel/Desktop/DiscordBotTry2/Sounds/grenade-plonk-sound-effect-tarkov-louder.mp3";
            audioPlayerManager.loadItem(localPath, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    audioPlayer.playTrack(track);
                    sink.success(); // Signal completion
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    List<AudioTrack> tracks = playlist.getSelectedTrack() != null ?
                            List.of(playlist.getSelectedTrack()) : playlist.getTracks();
                    if (!tracks.isEmpty()) {
                        audioPlayer.playTrack(tracks.get(0));
                        sink.success(); // Signal completion
                    } else {
                        sink.error(new RuntimeException("No tracks in the playlist."));
                    }
                }

                @Override
                public void noMatches() {
                    // Improved logging for no matches
                    System.err.println("No matches found for: " + trackUrl);
                    sink.error(new RuntimeException("No matches found for: " + trackUrl));
                }

                @Override
                public void loadFailed(FriendlyException e) {
                    // Improved logging for load failures
                    System.err.println("Failed to load track: " + trackUrl + " due to: " + e.getMessage());
                    sink.error(new RuntimeException("Failed to load track: " + trackUrl + " due to: " + e.getMessage()));
                }
            });
        });
    }

    // Disconnect from the voice channel
    public Mono<Void> disconnectFromVoiceChannel() {
        if (voiceConnection != null) {
            return voiceConnection.disconnect()
                    .doOnTerminate(() -> {
                        audioPlayer.stopTrack();
                        audioPlayer.destroy();
                    });
        }
        return Mono.empty();
    }
}
