package ru.kelcuprum.waterplayer.backend.output;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormatTools;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.Level;
import org.lwjgl.openal.AL10;
import ru.kelcuprum.waterplayer.WaterPlayer;
import ru.kelcuprum.waterplayer.backend.MusicPlayer;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

public class AudioOutput extends Thread {

    private static final Log log = LogFactory.getLog(AudioOutput.class);
    private final MusicPlayer musicPlayer;
    private final AudioFormat format;


    public AudioOutput(MusicPlayer musicPlayer) {
        super("Audio Player");
        this.musicPlayer = musicPlayer;
        format = AudioDataFormatTools.toAudioFormat(musicPlayer.getAudioDataFormat());
        setMixer(WaterPlayer.config.getString("SPEAKER", ""));
    }

    public void run() {
        WaterPlayer.log("running");
        log.info("running");
        try {
            final AudioPlayer player = musicPlayer.getAudioPlayer();
            final AudioDataFormat dataFormat = musicPlayer.getAudioDataFormat();
            final int[] stream = new int[]{AudioPlayerInputStream.createStream(player, dataFormat, dataFormat.frameDuration(), false).read()};

            int buffers = AL10.alGenBuffers();
            AL10.alBufferData(buffers, AL10.AL_FORMAT_STEREO16, stream, musicPlayer.getAudioDataFormat().sampleRate);

            int source = AL10.alGenSources();
            AL10.alSourcef(source, AL10.AL_GAIN, 1);
            AL10.alSource3f(source, AL10.AL_POSITION, 0, 0, 0);
            AL10.alSourcef(source, AL10.AL_BUFFER, buffers);
            AL10.alListenerf(AL10.AL_POSITION, 0);


            final long frameDuration = dataFormat.frameDuration();
            int chunkSize;

            AL10.alSourcePlay(source);
            WaterPlayer.log("Play source"+ AL10.alGetSourcef(source, AL10.AL_SOURCE_TYPE));

        } catch (final Exception ex) {
            WaterPlayer.log(ex.getLocalizedMessage(), Level.ERROR);
        }
    }

    public String[] getAudioDevices(){
        return List.of("Use vanilla settings").toArray(new String[0]);
    }
    public List<String> getAudioDevicesList(){ return List.of("Use vanilla settings"); }

    public void setMixer(String name){}
    public static boolean hasLinesOpen(Mixer mixer){
        return false;
    }

}
