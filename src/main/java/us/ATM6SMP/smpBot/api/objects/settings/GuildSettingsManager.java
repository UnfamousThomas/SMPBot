package us.ATM6SMP.smpBot.api.objects.settings;

import us.ATM6SMP.smpBot.SMPBot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuildSettingsManager {
    private static GuildSettingsManager instance;
    private static Map<Long, GuildSettings> guildSettingsMap = new HashMap<>();
    public static GuildSettingsManager getInstance() {
        if(instance == null) {
            instance = new GuildSettingsManager();
        }
        return instance;
    }

    public void load() {
        List<GuildSettings> list = SMPBot.getMongoManager().getGuildSettingsDAO().find().asList();
        for (GuildSettings guildSettings : list) {
            guildSettingsMap.put(guildSettings.getGuildId(), guildSettings);
        }
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

    private void save(GuildSettings guildSettings) {
        SMPBot.getMongoManager().getGuildSettingsDAO().save(guildSettings);
    }

    public void saveAll() {
        for (Map.Entry entry : guildSettingsMap.entrySet()) {
            save((GuildSettings) entry.getValue());
        }
    }


}
