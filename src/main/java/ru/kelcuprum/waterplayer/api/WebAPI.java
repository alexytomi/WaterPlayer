package ru.kelcuprum.waterplayer.api;

import com.google.gson.JsonObject;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import express.Express;
import express.middleware.CorsOptions;
import express.middleware.Middleware;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.GsonHelper;
import ru.kelcuprum.alinlib.AlinLogger;
import ru.kelcuprum.waterplayer.WaterPlayer;
import ru.kelcuprum.waterplayer.backend.WaterPlayerAPI;
import ru.kelcuprum.waterplayer.frontend.localization.MusicHelper;

import static java.lang.Integer.parseInt;

public class WebAPI {
    public static boolean state = false;
    public static boolean corsSetting = false;
    public static AlinLogger logger = new AlinLogger("WaterPlayer/WebAPI");
    public static Express app = new Express(WaterPlayer.apiConfig.getString("hostname", "127.0.0.1"));
    public static void run(){
        app = new Express(WaterPlayer.apiConfig.getString("hostname", "127.0.0.1"));
        if(!corsSetting) {
            corsSetting = true;
            CorsOptions corsOptions = new CorsOptions();
            corsOptions.setOrigin("*");
            corsOptions.setAllowCredentials(true);
            corsOptions.setHeaders(new String[]{"GET", "POST"});
        }
        app.use(Middleware.cors());
        app.use((req, res) -> {
            if(!WaterPlayer.apiConfig.getBoolean("enable", false)){
                JsonObject resp = new JsonObject();
                JsonObject error = new JsonObject();
                error.addProperty("code", 403);
                error.addProperty("codename", "Forbidden");
                error.addProperty("message", "Disabled by configs");
                resp.add("error", error);
                res.setStatus(403);
                res.json(resp);
            }
        });
        app.all("/", (req, res) -> {
            JsonObject resp = new JsonObject();
            resp.addProperty("message", "Hello, world!");
            resp.addProperty("version", FabricLoader.getInstance().getModContainer("waterplayer").get().getMetadata().getVersion().getFriendlyString());
            res.json(resp);
        });
        app.get("/current", (req, res) -> {
            JsonObject resp = new JsonObject();
            AudioTrack track = WaterPlayer.player.getAudioPlayer().getPlayingTrack();
            resp.addProperty("state", track == null ? "nothing" : WaterPlayer.player.isPaused() ? "paused" : "listening");
            resp.addProperty("volume", WaterPlayer.player.getVolume());
            resp.addProperty("repeat", WaterPlayer.player.getTrackScheduler().getRepeatStatus());
            if(track == null) resp.add("track", null);
            else {
                JsonObject trackInfo = new JsonObject();
                trackInfo.addProperty("live", track.getInfo().isStream);
                trackInfo.addProperty("service", MusicHelper.getService(track));
                trackInfo.addProperty("title", MusicHelper.getTitle(track));
                if(!MusicHelper.isAuthorNull(track)) trackInfo.addProperty("author", MusicHelper.getAuthor(track));
                trackInfo.addProperty("artwork", MusicHelper.isFile(track) || track.getInfo().artworkUrl == null ? WaterPlayerAPI.getArtwork(track) : track.getInfo().artworkUrl);

                double z = track.getInfo().isStream ? 1.0 : (double) WaterPlayer.player.getAudioPlayer().getPlayingTrack().getPosition() / WaterPlayer.player.getAudioPlayer().getPlayingTrack().getDuration();
                trackInfo.addProperty("progress", z);
                if(!track.getInfo().isStream){
                    trackInfo.addProperty("position", track.getPosition());
                    trackInfo.addProperty("duration", track.getDuration());
                }
                resp.add("track", trackInfo);
            }
            res.json(resp);
        });
        app.all((req, res) -> {
            res.setStatus(404);
            res.json(Objects.NOT_FOUND);
        });
        app.listen(parseInt(WaterPlayer.apiConfig.getString("port", "2264")));
        logger.log("API Started");
        logger.log("Open: http://localhost:%s", WaterPlayer.apiConfig.getNumber("port", 2264).intValue());
        state = true;
    }
    public static void stop(){
        if(state) {
            app.stop();
            logger.log("API Stopped");
        } else logger.warn("API not running");
    }

    public interface Objects {
        JsonObject NOT_FOUND = GsonHelper.parse("{\"error\":{\"code\":404,\"codename\":\"Not found\",\"message\":\"Method not found\"}}");
        JsonObject INTERNAL_SERVER_ERROR = GsonHelper.parse("{\"error\":{\"code\":500,\"codename\":\"Internal Server Error\",\"message\":\"\"}}");
        JsonObject UNAUTHORIZED = GsonHelper.parse("{\"error\": {\"code\": 401,\"codename\": \"Unauthorized\",\"message\": \"You not authorized\"}}");
        JsonObject BAD_REQUEST = GsonHelper.parse("{\"error\": {\"code\": 400,\"codename\": \"Bad Request\",\"message\": \"The required arguments are missing!\"}}");
    }
}
