package ru.kelcuprum.waterplayer.frontend.rpc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.ActivityType;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.User;
import ru.kelcuprum.waterplayer.backend.OpenAL.AudioTrack;
import org.apache.logging.log4j.Level;
import ru.kelcuprum.waterplayer.WaterPlayer;
import ru.kelcuprum.waterplayer.backend.WaterPlayerAPI;
import ru.kelcuprum.waterplayer.frontend.localization.MusicHelper;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class DiscordIntegration {
    public static IPCClient client;
    public static RichPresence lastPresence;
    public static boolean connected = false;
    public static boolean empty = true;
    private static final Timer TIMER = new Timer();
    private static String lastException;

    public DiscordIntegration() {
        TIMER.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    update();
                } catch (Exception ex) {
                    if (lastException == null || !lastException.equals(ex.getMessage())) {
                        lastException = ex.getMessage();
                        WaterPlayer.log(ex.getLocalizedMessage(), Level.ERROR);
                        RichPresence.Builder presence = new RichPresence.Builder()
                                .setActivityType(ActivityType.Competing)
                                .setDetails("There was an error")
                                .setState("Check the logs & send a report")
                                .setLargeImage("https://wf.kelcu.ru/mods/waterplayer/icons/seadrive.gif");
                        if (connected) send(presence.build());
                    }
                }
            }
        }, 250, 250);
    }

    public void registerApplication() {
        client = new IPCClient(
                //#if WALTER == 0
                1197963953695903794L
                //#else
                //$$ 1277205430275145758L
                //#endif
        );
        setupListener();
        try {
            client.connect();
        } catch (Exception ex) {
            WaterPlayer.log(ex.getLocalizedMessage(), Level.ERROR);
        }
    }

    public void exitApplication() {
        if (connected) client.close();
    }

    public void setupListener() {
        client.setListener(new IPCListener() {
            @Override
            public void onPacketSent(IPCClient ipcClient, Packet packet) {

            }

            @Override
            public void onPacketReceived(IPCClient ipcClient, Packet packet) {

            }

            @Override
            public void onActivityJoin(IPCClient ipcClient, String s) {

            }

            @Override
            public void onActivitySpectate(IPCClient ipcClient, String s) {

            }

            @Override
            public void onActivityJoinRequest(IPCClient ipcClient, String s, User user) {

            }

            @Override
            public void onReady(IPCClient client) {
                WaterPlayer.log("The mod has been connected to Discord", Level.DEBUG);
                connected = true;
            }

            @Override
            public void onClose(IPCClient ipcClient, JsonObject jsonObject) {
                connected = false;
            }

            @Override
            public void onDisconnect(IPCClient ipcClient, Throwable throwable) {
                WaterPlayer.log("The mod has been pulled from Discord", Level.DEBUG);
                WaterPlayer.log(String.format("Reason: %s", throwable.getLocalizedMessage()), Level.DEBUG);
                connected = false;
            }
        });
    }
    public HashMap<String, JsonObject> urls = new HashMap<>();
    public void update() {
        AudioTrack track = WaterPlayer.player.getAudioPlayer().getPlayingTrack();
        if (track == null || !WaterPlayer.config.getBoolean("DISCORD", false) || (WaterPlayer.player.getAudioPlayer().isPaused() && WaterPlayer.config.getBoolean("DISCORD.HIDE_IN_PAUSE", false))) send(null);
        else {
            RichPresence.Builder builder = new RichPresence.Builder().setActivityType(ActivityType.Listening);
            String icon = MusicHelper.isFile() ? "file" : (track.getInfo().artworkUrl == null || track.getInfo().artworkUrl.isBlank()) ? "no_icon" : track.getInfo().artworkUrl;
            if (!MusicHelper.isAuthorNull(track) && !MusicHelper.isTitleNull(track) && MusicHelper.isFile(track)) {
                String apiIcon = WaterPlayerAPI.getArtwork(track);
                if(!apiIcon.isBlank()) icon = apiIcon;
            }
            builder.setLargeImage(icon, getLargeImageText(track))
                    .setDetails(MusicHelper.getTitle())
                    .setState(MusicHelper.getAuthor());
            getYonKaGorMoment(track, builder);
            long start = System.currentTimeMillis() - MusicHelper.getPosition();
            if (WaterPlayer.player.getAudioPlayer().isPaused()) builder.setSmallImage("paused");
            else {
                if (!MusicHelper.isAuthorNull(track) && WaterPlayer.config.getBoolean("DISCORD.AUTHOR_AVATAR", true)) {
                    String apiIcon = WaterPlayerAPI.getAuthorAvatar(track);
                    if(!apiIcon.isBlank() && (icon.equalsIgnoreCase("file") || icon.equalsIgnoreCase("no_icon")))
                        builder.setLargeImage(apiIcon, getLargeImageText(track));
                    else if(!apiIcon.isBlank() && !apiIcon.equals(icon)) builder.setSmallImage(apiIcon, MusicHelper.getAuthor(track));
                }
                if(WaterPlayer.config.getBoolean("DISCORD.TIME", true)){
                    builder.setStartTimestamp(parseSeconds(start));
                    if (!MusicHelper.getIsLive()) builder.setEndTimestamp(parseSeconds(start + MusicHelper.getDuration()));
                }
            }
            if (!MusicHelper.isFile(track) && WaterPlayer.config.getBoolean("DISCORD.BUTTON", true)) {
                JsonArray buttons = new JsonArray();
                JsonObject button = new JsonObject();
                button.addProperty("label", MusicHelper.getServiceName(MusicHelper.getService(track)).getString());
                button.addProperty("url", track.getInfo().uri);
                buttons.add(button);
                builder.setButtons(buttons);
            }
            send(builder.build());
        }
    }

    public long parseSeconds(long mills) {
        return (mills - (mills % 1000)) / 1000;
    }

    public void send(RichPresence presence) {
        if (!empty && connected && presence == null) {
            if (lastPresence != null) exitApplication();
            lastPresence = null;
            empty = true;
        } else if (presence != null && (lastPresence == null || (!lastPresence.toJson().toString().equalsIgnoreCase(presence.toJson().toString())))) {
            if (empty) registerApplication();
            empty = false;
            try {
                if (connected) client.sendRichPresence(presence);
                lastPresence = presence;
            } catch (Exception ex) {
                WaterPlayer.log(ex.getMessage() == null ? ex.getClass().getName() : ex.getMessage(), Level.ERROR);
            }
        }
    }
    protected String getLargeImageText(AudioTrack track){
        JsonObject trackInfo = WaterPlayerAPI.getTrackInfo(track, false);
        String album = "";
        if(trackInfo.has("track") && WaterPlayer.config.getBoolean("DISCORD.ALBUM", true)){
            JsonObject info = trackInfo.getAsJsonObject("track");
            album = info.has("album") ? info.get("album").getAsString() : "";
            if(album.equalsIgnoreCase(info.get("title").getAsString())) album = "";
        }
        return WaterPlayer.config.getBoolean("DISCORD.SERVICE", false) ? MusicHelper.getServiceName(MusicHelper.getService(track)).getString() : album;
    }
    protected void getYonKaGorMoment(AudioTrack track, RichPresence.Builder builder) {
        if (!MusicHelper.getAuthor(track).equals("YonKaGor") || !WaterPlayer.config.getBoolean("DISCORD.YONKACATS", true)) return;
        switch (MusicHelper.getTitle(track)) {
            case "Top 10 Things to Do Before You Die", "Top 10 Things To Do Before You Die",
                 "[TW] Top 10 Things To Do Before You Die (Censored)" ->
                    builder.setLargeImage("https://wf.kelcu.ru/mods/waterplayer/icons/seadrive.gif", "HowTo: be happy 🔍");
            case "You're Just Like Pop Music" ->
                    builder.setLargeImage("https://wf.kelcu.ru/mods/waterplayer/icons/tetra.gif", getLargeImageText(track));
            case "Circus Hop", "Circus Hop [TW]" ->
                    builder.setLargeImage("https://wf.kelcu.ru/mods/waterplayer/icons/clownfish.png", getLargeImageText(track));
        }
    }
}
