package ru.kelcuprum.waterplayer.backend.melody;

import ru.kelcuprum.alinlib.config.Config;
import ru.kelcuprum.waterplayer.backend.MusicPlayer;
import ru.kelcuprum.waterplayer.backend.melody.OpenAL.CustomAudioPlayer;
import ru.kelcuprum.waterplayer.backend.melody.OpenAL.CustomAudioPlayerManager;
import ru.kelcuprum.waterplayer.backend.melody.OpenAL.CustomTrackScheduler;
import ru.kelcuprum.waterplayer.backend.melody.OpenAL.CustomAudioOutput;

public class CustomMusicPlayer extends MusicPlayer {

    // Audio Player Management
    private final CustomAudioPlayerManager audioPlayerManager; // Manages what is being played
    private final CustomAudioPlayer audioPlayer; // Manages control over currently playing

    // Track Scheduler
    private final CustomTrackScheduler trackScheduler;

    // Audio Output
    private final CustomAudioOutput audioOutput;

    // Volume Control
    private int volume;

    // Configuration
    private Config config;

    public CustomMusicPlayer() {
        // Initialize components
        audioPlayerManager = new CustomAudioPlayerManager();
        audioPlayer = audioPlayerManager.createPlayer();
        trackScheduler = new CustomTrackScheduler(audioPlayer);
        audioOutput = new CustomAudioOutput(this);
        volume = config.getNumber("CURRENT_MUSIC_VOLUME", 3).intValue();

        // Set initial volume
        audioPlayer.setVolume(volume);

        // Register sources
        registerSources();
    }

    // Method to register audio sources
    private void registerSources() {
        // Implement source registration logic
    }

    // Method to load and play music
    public void loadMusic(String url) {
        // Implement music loading logic
    }
    public void updateFilter() {

    }

}



