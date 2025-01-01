/**
 * Thanks to VSETH-GECO for this amazing audio consumer class for lavaplayer (It is kind of changed) MIT License
 * Copyright (c) 2017 VSETH-GECO Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The
 * above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package ru.kelcuprum.waterplayer.backend.melody.OpenAL;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormatTools;
import ru.kelcuprum.waterplayer.WaterPlayer;
import ru.kelcuprum.waterplayer.backend.MusicPlayer;
import ru.kelcuprum.waterplayer.backend.melody.CustomMusicPlayer;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

public class CustomAudioOutput extends Thread {

    private final CustomMusicPlayer musicPlayer;
    private final DataLine.Info speakerInfo;
    private Mixer mixer;
    private SourceDataLine sourceLine;

    public CustomAudioOutput(CustomMusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
        speakerInfo = new DataLine.Info(SourceDataLine.class, null);
    }

    @Override
    public void run() {
        // No implementation
    }

    public void setMixer(String name) {
        // No implementation
    }

    private boolean createLine() {
        // No implementation
        return false;
    }

    private void closeLine() {
        // No implementation
    }

    public String[] getAudioDevices() {
        // No implementation
        return new String[0];
    }

    public List<String> getAudioDevicesList() {
        // No implementation
        return new ArrayList<>();
    }

    private Mixer findMixer(String name, Line.Info lineInfo) {
        // No implementation
        return null;
    }

    public static boolean hasLinesOpen(Mixer mixer) {
        // No implementation
        return false;
    }
}
