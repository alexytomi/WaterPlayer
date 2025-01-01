package ru.kelcuprum.waterplayer.backend.melody;


import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.io.MessageInput;
import com.sedmelluq.discord.lavaplayer.tools.io.MessageOutput;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.DecodedTrackHolder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

public class OpenALAudioPlayerManager implements AudioPlayerManager {

    @Override
    public void shutdown() {

    }

    @Override
    public void enableGcMonitoring() {

    }

    @Override
    public void registerSourceManager(AudioSourceManager sourceManager) {

    }

    @Override
    public void registerSourceManagers(AudioSourceManager... sourceManagers) {
        AudioPlayerManager.super.registerSourceManagers(sourceManagers);
    }

    @Override
    public <T extends AudioSourceManager> T source(Class<T> klass) {
        return null;
    }

    @Override
    public List<AudioSourceManager> getSourceManagers() {
        return List.of();
    }

    @Override
    public Future<Void> loadItem(String identifier, AudioLoadResultHandler resultHandler) {
        return AudioPlayerManager.super.loadItem(identifier, resultHandler);
    }

    @Override
    public Future<Void> loadItem(AudioReference reference, AudioLoadResultHandler resultHandler) {
        return null;
    }

    @Override
    public void loadItemSync(String identifier, AudioLoadResultHandler resultHandler) {
        AudioPlayerManager.super.loadItemSync(identifier, resultHandler);
    }

    @Override
    public void loadItemSync(AudioReference reference, AudioLoadResultHandler resultHandler) {

    }

    @Override
    public @Nullable AudioItem loadItemSync(String reference) {
        return AudioPlayerManager.super.loadItemSync(reference);
    }

    @Override
    public @Nullable AudioItem loadItemSync(AudioReference reference) {
        return null;
    }

    @Override
    public Future<Void> loadItemOrdered(Object orderingKey, String identifier, AudioLoadResultHandler resultHandler) {
        return AudioPlayerManager.super.loadItemOrdered(orderingKey, identifier, resultHandler);
    }

    @Override
    public Future<Void> loadItemOrdered(Object orderingKey, AudioReference reference, AudioLoadResultHandler resultHandler) {
        return null;
    }

    @Override
    public void encodeTrack(MessageOutput stream, AudioTrack track) throws IOException {

    }

    @Override
    public DecodedTrackHolder decodeTrack(MessageInput stream) throws IOException {
        return null;
    }

    @Override
    public AudioConfiguration getConfiguration() {
        return null;
    }

    @Override
    public boolean isUsingSeekGhosting() {
        return false;
    }

    @Override
    public void setUseSeekGhosting(boolean useSeekGhosting) {

    }

    @Override
    public int getFrameBufferDuration() {
        return 0;
    }

    @Override
    public void setFrameBufferDuration(int frameBufferDuration) {

    }

    @Override
    public void setTrackStuckThreshold(long trackStuckThreshold) {

    }

    @Override
    public void setPlayerCleanupThreshold(long cleanupThreshold) {

    }

    @Override
    public void setItemLoaderThreadPoolSize(int poolSize) {

    }

    @Override
    public AudioPlayer createPlayer() {
        return null;
    }

    @Override
    public void setHttpRequestConfigurator(Function<RequestConfig, RequestConfig> configurator) {

    }

    @Override
    public void setHttpBuilderConfigurator(Consumer<HttpClientBuilder> configurator) {

    }
}
