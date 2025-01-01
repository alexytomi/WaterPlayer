package ru.kelcuprum.waterplayer.backend.melody;

import com.sedmelluq.discord.lavaplayer.filter.PcmFilterFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import de.keksuccino.melody.resources.audio.*;
import org.lwjgl.openal.*;

import java.nio.FloatBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class OpenALAudioPlayer implements AudioPlayer {
    private int source;
    private AudioTrack playingTrack;
    private boolean paused;
    private int volume;

    public OpenALAudioPlayer() {
        source = AL10.alGenSources();
        volume = 100; // Default volume
    }

    /**
     * @return Currently playing track
     */
    @Override
    public AudioTrack getPlayingTrack() {
        return playingTrack;
    }

    /**
     * @param track The track to start playing
     */
    @Override
    public void playTrack(AudioTrack track) {
        stopTrack();
        this.playingTrack = track;
        // Load track data into OpenAL buffer and queue it to the source
        // This part depends on how you handle track data
    }

    /**
     * @param track       The track to start playing, passing null will stop the current track and return false
     * @param noInterrupt Whether to only start if nothing else is playing
     * @return True if the track was started
     */
    @Override
    public boolean startTrack(AudioTrack track, boolean noInterrupt) {
        if (noInterrupt && AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) {
            return false;
        }
        playTrack(track);
        AL10.alSourcePlay(source);
        return true;
    }

    /**
     * Stop currently playing track.
     */
    @Override
    public void stopTrack() {
        AL10.alSourceStop(source);
        playingTrack = null;
    }

    /**
     * @return 
     */
    @Override
    public int getVolume() {
        return volume;
    }

    /**
     * @param volume 
     */
    @Override
    public void setVolume(int volume) {
        this.volume = volume;
        AL10.alSourcef(source, AL10.AL_GAIN, volume / 100.0f);
    }

    /**
     * @param factory 
     */
    @Override
    public void setFilterFactory(PcmFilterFactory factory) {

    }

    /**
     * @param duration 
     */
    @Override
    public void setFrameBufferDuration(Integer duration) {

    }

    /**
     * @return Whether the player is paused
     */
    @Override
    public boolean isPaused() {
        return false;
    }

    /**
     * @param value True to pause, false to resume
     */
    @Override
    public void setPaused(boolean value) {

    }

    /**
     * Destroy the player and stop playing track.
     */
    @Override
    public void destroy() {

    }

    /**
     * Add a listener to events from this player.
     *
     * @param listener New listener
     */
    @Override
    public void addListener(AudioEventListener listener) {

    }

    /**
     * Remove an attached listener using identity comparison.
     *
     * @param listener The listener to remove
     */
    @Override
    public void removeListener(AudioEventListener listener) {

    }

    /**
     * Check if the player should be "cleaned up" - stopped due to nothing using it, with the given threshold.
     *
     * @param threshold Threshold in milliseconds to use
     */
    @Override
    public void checkCleanup(long threshold) {

    }

    /**
     * @return Provided frame, or null if none available
     */
    @Override
    public AudioFrame provide() {
        return null;
    }

    /**
     * @param timeout Specifies the maximum time to wait for data. Pass 0 for non-blocking mode.
     * @param unit    Specifies the time unit of the maximum wait time.
     * @return Provided frame. In case wait time is above zero, null indicates that no data is not available at the
     * current moment, otherwise null means the end of the track.
     * @throws TimeoutException     When wait time is above zero, but no track info is found in that time.
     * @throws InterruptedException When interrupted externally (or for seek/stop).
     */
    @Override
    public AudioFrame provide(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
        return null;
    }

    /**
     * @param targetFrame Frame to update with the details and data of the provided frame.
     * @return <code>true</code> if a frame was provided.
     */
    @Override
    public boolean provide(MutableAudioFrame targetFrame) {
        return false;
    }

    /**
     * @param targetFrame Frame to update with the details and data of the provided frame.
     * @param timeout     Timeout.
     * @param unit        Time unit for the timeout value.
     * @return <code>true</code> if a frame was provided.
     * @throws TimeoutException     If no frame became available within the timeout.
     * @throws InterruptedException When interrupted externally (or for seek/stop).
     */
    @Override
    public boolean provide(MutableAudioFrame targetFrame, long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
        return false;
    }
}
