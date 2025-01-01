package ru.kelcuprum.waterplayer.backend;

import ru.kelcuprum.alinlib.config.Config;
import ru.kelcuprum.waterplayer.backend.OpenAL.*;


public class MusicPlayer {

    // Audio Player Management
    private final AudioPlayerManager audioPlayerManager; // Manages what is being played
    private final AudioPlayer audioPlayer; // Manages control over currently playing

    // Track Scheduler
    private final TrackScheduler trackScheduler;

    // Audio Output
    private final AudioOutput audioOutput;

    // Volume Control
    private int volume;

    // Configuration
    private Config config;

    public MusicPlayer() {
        // Initialize components
        audioPlayerManager = new AudioPlayerManager();
        audioPlayer = audioPlayerManager.createPlayer();
        trackScheduler = new TrackScheduler(audioPlayer);
        audioOutput = new AudioOutput(this);
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



