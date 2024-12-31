package ru.kelcuprum.waterplayer.backend.melody.OpenAL;

public class CustomAudioPlayer {

        private int volume;
        private boolean isPlaying;

        public void play() {
            // Implement play logic
            isPlaying = true;
        }

        public void pause() {
            // Implement pause logic
            isPlaying = false;
        }

        public void stop() {
            // Implement stop logic
            isPlaying = false;
        }

        public void setVolume(int volume) {
            this.volume = volume;
            // Implement volume control logic
        }

        public int getVolume() {
            return volume;
        }

        public boolean isPlaying() {
            return isPlaying;
        }
    }
