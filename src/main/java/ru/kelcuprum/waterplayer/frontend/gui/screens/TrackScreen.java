package ru.kelcuprum.waterplayer.frontend.gui.screens;

import com.github.topi314.lavalyrics.lyrics.AudioLyrics;
import ru.kelcuprum.waterplayer.backend.OpenAL.AudioTrack;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.Colors;
import ru.kelcuprum.alinlib.gui.components.ConfigureScrolWidget;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.text.TextBuilder;
import ru.kelcuprum.alinlib.gui.components.buttons.Button;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.toast.ToastBuilder;
import ru.kelcuprum.waterplayer.WaterPlayer;
import ru.kelcuprum.waterplayer.backend.playlist.Playlist;
import ru.kelcuprum.waterplayer.frontend.gui.LyricsHelper;
import ru.kelcuprum.waterplayer.frontend.gui.SafeLyrics;
import ru.kelcuprum.waterplayer.frontend.gui.components.LyricsBox;
import ru.kelcuprum.waterplayer.frontend.localization.MusicHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.kelcuprum.alinlib.gui.Icons.DONT;

public class TrackScreen extends Screen {
    protected final Screen parent;
    protected final AudioTrack track;
    protected final boolean isFile;
    protected AudioLyrics lyrics;
    protected boolean lyricsEnable = false;
    protected boolean showLyrics = false;
    protected boolean showPlaylist = false;

    public TrackScreen(Screen parent, AudioTrack track) {
        super(Component.empty());
        this.parent = parent;
        this.track = track;
        if (this.track == null) {
            this.isFile = false;
            onClose();
            return;
        }
        this.isFile = new File(track.getInfo().uri).exists();
        this.lyrics = LyricsHelper.getLyrics(track);
    }

    Button lyricsButton;
    Button backButton;
    LyricsBox lyricsBox;
    int x = 10;
    int iconSize = 5 + (14 * 3);
    int lyricsSize = 200;
    int componentSize = 310;

    @Override
    protected void init() {
        int iconSize = 5 + ((font.lineHeight + 5) * 3);
        int pageSize = this.width;
        lyricsSize = Math.min(width / 2, 200);
        if (showLyrics || showPlaylist) pageSize -= lyricsSize;
        componentSize = Math.min(310, pageSize - 10);
        x = (pageSize - componentSize) / 2;
        if (showLyrics || showPlaylist) x += lyricsSize;
        if (showPlaylist) initPlaylist();
        else if (showLyrics) {
            addRenderableWidget(new TextBuilder(Localization.getText("waterplayer.track.lyrics.title")).setPosition(5, 5).setPosition(lyricsSize-10, 20));
            lyricsBox = addRenderableWidget(new LyricsBox(10, 35, lyricsSize - 20, height - (lyricsEnable ? 95 : 70), Component.empty())).setLyrics(Component.literal(lyricsEnable ? lyrics.getText() != null ? lyrics.getText().replace("\r", "") : "404: Not found" : "404: Not found"));
            if(lyricsEnable) addRenderableWidget(new ButtonBuilder(Component.translatable("waterplayer.track.lyrics.copy"), (onPress) -> {
                AlinLib.MINECRAFT.keyboardHandler.setClipboard(lyrics.getText() == null ? "" : lyrics.getText());
                WaterPlayer.getToast().setMessage(Component.translatable("waterplayer.track.lyrics.copy.toast")).buildAndShow();
            }).setPosition(5, height - 25).setSize(lyricsSize - 10, 20).build());

            String id = WaterPlayer.parseFileSystem(track.getSourceManager().getSourceName() + "_" + track.getIdentifier());
            addRenderableWidget(new ButtonBuilder(Component.translatable("waterplayer.track.lyrics.create_srt"), (onPress) -> {
                try{
                    Util.getPlatform().openFile(new File(LyricsHelper.saveSRT(track, lyricsEnable ? lyrics.getText() != null ? lyrics.getText().replace("\r", "") : "Example text" : "Example text")));
                    WaterPlayer.getToast().setMessage(Component.translatable("waterplayer.track.lyrics.created_srt")).buildAndShow();
                } catch (Exception e){
                    WaterPlayer.getToast().setMessage(Component.literal(e.getMessage() == null ? e.getClass().getName() : e.getMessage()))
                            .setType(ToastBuilder.Type.ERROR).setIcon(DONT).buildAndShow();
                }
            }).setPosition(5, height - (lyricsEnable ? 50 : 25)).setSize(lyricsSize - 10, 20).build());
        }
        addRenderableWidget(new ButtonBuilder(Component.translatable(isFile ? "waterplayer.track.open_file" : "waterplayer.track.open_link"), (huy) -> {
            if (isFile) Util.getPlatform().openFile(new File(track.getInfo().uri));
            else Util.getPlatform().openUri(track.getInfo().uri);
        }).setWidth(componentSize / 2 - 2).setPosition(x, height / 2 - 10).build());
        addRenderableWidget(new ButtonBuilder(Component.translatable("waterplayer.track.copy_link"), (huy) -> AlinLib.MINECRAFT.keyboardHandler.setClipboard(track.getInfo().uri)).setWidth(componentSize / 2 - 2).setPosition(x + componentSize / 2 + 2, height / 2 - 10).build());

        int y = height / 2 + 15;

        addRenderableWidget(new ButtonBuilder(Component.translatable("waterplayer.track.play"), (huy) -> {
            WaterPlayer.player.loadMusic(track.getInfo().uri, false);
            onClose();
        }).setWidth(componentSize / 2 - 2).setPosition(x, y).build());
        addRenderableWidget(new ButtonBuilder(Component.translatable("waterplayer.track.add_playlist"), (huy) -> {
            showPlaylist = true;
            rebuildWidgets();
        }).setWidth(componentSize / 2 - 2).setPosition(x + componentSize / 2 + 2, y).build());
        y += 25;

        if(!track.getInfo().isStream) {
            this.lyricsButton = (Button) addRenderableWidget(new ButtonBuilder(Component.translatable(showLyrics ? "waterplayer.track.hide_lyrics" : "waterplayer.track.lyrics"), (huy) -> {
                showLyrics = !showLyrics;
                rebuildWidgets();
            }).setWidth(componentSize / 2 - 2).setPosition(x, y).build());
        }
        backButton = (Button) addRenderableWidget(new ButtonBuilder(CommonComponents.GUI_BACK, (huy) -> onClose()).setWidth(track.getInfo().isStream ? componentSize : componentSize / 2 - 2).setPosition(track.getInfo().isStream ? x : x + componentSize / 2 + 2, y).build());
        y+=25;
        if(MusicHelper.isFile(track)) addRenderableWidget(new ButtonBuilder(Component.translatable("waterplayer.editor"), (huy) -> WaterPlayer.openTrackEditor(new File(track.getInfo().uri))).setWidth(componentSize).build()).setPosition(x, y);
        int textY = height / 2 - 15 - iconSize + 5;
        addRenderableWidget(new TextBuilder(Component.literal(MusicHelper.getTitle(track))).setAlign(TextBuilder.ALIGN.LEFT).setPosition(x + iconSize + 5, textY).setSize(componentSize - (iconSize + 10), font.lineHeight).build());
        textY += (font.lineHeight + 5);
        if (!MusicHelper.getAuthor(track).isBlank()) {
            addRenderableWidget(new TextBuilder(Component.translatable("waterplayer.track.author", MusicHelper.getAuthor(track))).setAlign(TextBuilder.ALIGN.LEFT).setPosition(x + iconSize + 5, textY).setSize(componentSize - (iconSize + 10), font.lineHeight).build());
            textY += (font.lineHeight + 5);
        }
        addRenderableWidget(new TextBuilder(Component.translatable("waterplayer.track.service", MusicHelper.getServiceName(MusicHelper.getService(track)))).setAlign(TextBuilder.ALIGN.LEFT).setPosition(x + iconSize + 5, textY).setSize(componentSize - (iconSize + 10), font.lineHeight).build());
    }

    private ConfigureScrolWidget scroller_panel;
    private TextBox titleW;
    private Button back;
    private List<AbstractWidget> playlists;

    public void initPlaylist() {
        playlists = new ArrayList<>();
        titleW = (TextBox) addRenderableWidget(new TextBuilder(Localization.getText("waterplayer.track.playlists")).setPosition(5, 5).setSize(lyricsSize-10, 30).build());
        back = (Button) addRenderableWidget(new ButtonBuilder(Component.translatable("waterplayer.track.playlists.hide"), (onPress) -> {
                        showPlaylist = false;
                        rebuildWidgets();
                    }).setPosition(5, height - 25).setSize(lyricsSize - 10, 20).build());
        File playlistsFolder = AlinLib.MINECRAFT.gameDirectory.toPath().resolve(WaterPlayer.getPath()+"/playlists").toFile();
        int yP = 40;
        if (playlistsFolder.exists() && playlistsFolder.isDirectory()) {
            for (File playlist : Objects.requireNonNull(playlistsFolder.listFiles())) {
                if (playlist.isFile() && playlist.getName().endsWith(".json")) {
                    try {
                        Playlist playlistObject = new Playlist(playlist.toPath());
                        playlists.add(new ButtonBuilder(Component.translatable("waterplayer.playlists.value", playlistObject.title, playlistObject.author), (s) -> {
                            playlistObject.addUrl(track.getInfo().uri);
                            WaterPlayer.getToast().setMessage(Component.translatable("waterplayer.track.playlists.added", playlistObject.title)).buildAndShow();
                            onClose();
                        }).setSize(lyricsSize - 10, 20).setPosition(5, yP).build());
                        yP += 25;
                    } catch (Exception e) {
                        WaterPlayer.log(e.getLocalizedMessage(), Level.ERROR);
                    }
                }
            }
        }
        addRenderableWidgets(playlists);

        this.scroller_panel = addRenderableWidget(new ConfigureScrolWidget(0, 0, 4, this.height, Component.empty(), scroller -> {
            scroller.innerHeight = 5;
            titleW.setY((int) (scroller.innerHeight - scroller.scrollAmount()));
            scroller.innerHeight += titleW.getHeight() + 5;

            for (AbstractWidget widget : playlists) {
                if (widget.visible) {
                    widget.setY((int) (scroller.innerHeight - scroller.scrollAmount()));
                    scroller.innerHeight += (widget.getHeight() + 5);
                } else widget.setY(-widget.getHeight());
            }
            if (scroller.innerHeight >= height - 25) {
                back.setY((int) (scroller.innerHeight - scroller.scrollAmount()));
                scroller.innerHeight += (20);
            } else back.setY(height - 25);
        }));
    }

    protected void addRenderableWidgets(@NotNull List<AbstractWidget> widgets) {
        for (AbstractWidget widget : widgets) {
            this.addRenderableWidget(widget);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        //#if MC < 12002
        //$$ renderBackground(guiGraphics);
        //#endif
        super.render(guiGraphics, i, j, f);
        guiGraphics.blit(
                //#if MC >= 12102
                RenderType::guiTextured,
                //#endif
                MusicHelper.getThumbnail(track), x, height / 2 - 15 - iconSize, 0.0F, 0.0F, iconSize, iconSize, iconSize, iconSize);
    }

    @Override
    //#if MC >= 12002
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderBackground(guiGraphics, i, j, f);
        //#elseif MC < 12002
        //$$ public void renderBackground(GuiGraphics guiGraphics) {
        //$$         super.renderBackground(guiGraphics);
        //#endif
        if (showLyrics || showPlaylist) {
            guiGraphics.fill(5, 5, lyricsSize-5, 25, Colors.BLACK_ALPHA);
            guiGraphics.fill(5, 30, lyricsSize-5, height-(lyricsEnable ? 55 : 30), Colors.BLACK_ALPHA);
        }
    }

    @Override
    public void tick() {
        if (showPlaylist && scroller_panel != null) scroller_panel.onScroll.accept(scroller_panel);
        this.lyrics = LyricsHelper.getLyrics(track);
        this.lyricsEnable = lyrics != null && lyrics.getText() != null && !(lyrics instanceof SafeLyrics);
        super.tick();
    }

    @Override
    //#if MC >= 12002
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        boolean scr = super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        if (showPlaylist) {
            if (mouseX <= 200) scr = scroller_panel.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        } else if (showLyrics) {
            if ((mouseX >= 5 && mouseX <= 195) && (mouseY >= 40 && mouseY <= height - 30))
                scr = lyricsBox.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }
        return scr;
    }
    //#elseif MC < 12002
    //$$ public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
    //$$         boolean scr = super.mouseScrolled(mouseX, mouseY, scrollY);
    //$$         if (showPlaylist) {
    //$$             if (mouseX <= 200) scr = scroller_panel.mouseScrolled(mouseX, mouseY, scrollY);
    //$$         } else if (showLyrics) {
    //$$             if ((mouseX >= 5 && mouseX <= 195) && (mouseY >= 40 && mouseY <= height - 30)) {
    //$$                 scr = lyricsBox.mouseScrolled(mouseX, mouseY, scrollY);
    //$$             }
    //$$         }
    //$$         return scr;
    //$$     }
    //#endif

    public void onClose() {
        assert this.minecraft != null;
        this.minecraft.setScreen(parent);
    }
}
