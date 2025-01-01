package ru.kelcuprum.waterplayer.backend.melody.OpenAL;

import java.util.LinkedList;
import java.util.Queue;

public class CustomTrackScheduler {

    private final CustomAudioPlayer audioPlayer;
    private final Queue<String> trackQueue = new LinkedList<>();

    public CustomTrackScheduler(CustomAudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public void addTrack(String track) {
        trackQueue.add(track);
    }

    public void nextTrack() {
        if (!trackQueue.isEmpty()) {
            String nextTrack = trackQueue.poll();
            // Implement logic to play the next track
            audioPlayer.play();
        }
    }

    public void backTrack() {
        // Implement logic to play the previous track
    }

    public void shuffle() {
        // Implement logic to shuffle the track queue
    }
}
