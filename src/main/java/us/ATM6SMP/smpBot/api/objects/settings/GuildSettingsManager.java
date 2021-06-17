package us.ATM6SMP.smpBot.api.objects.settings;

import us.ATM6SMP.smpBot.SMPBot;

import java.util.HashMap;
import java.util.Map;

public class GuildSettingsManager {
    public static GuildSettingsManager instance;
    private static Map<Long, GuildSettings> guildSettingsMap = new HashMap<>();
    public static GuildSettingsManager getInstance() {
        if(instance == null) {
            instance = new GuildSettingsManager();
        }
        return instance;
    }

    public GuildSettings getGuildSettings(Long id) {
        GuildSettings guildSettings;
        if(!(guildSettingsMap.containsKey(id))) {
            guildSettings = new GuildSettings();
            guildSettings.setGuildId(id);
            SMPBot.getMongoManager().getGuildSettingsDAO().save(guildSettings);
        } else {
            guildSettings = guildSettingsMap.get(id);
        }

        return guildSettings;
    }
}
