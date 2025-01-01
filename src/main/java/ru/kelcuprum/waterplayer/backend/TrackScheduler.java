package ru.kelcuprum.waterplayer.backend;

import ru.kelcuprum.waterplayer.backend.OpenAL.AudioPlayer;

import java.util.LinkedList;
import java.util.Queue;

public class TrackScheduler {

    private final AudioPlayer audioPlayer;
    private final Queue<String> trackQueue = new LinkedList<>();

    public TrackScheduler(AudioPlayer audioPlayer) {
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
