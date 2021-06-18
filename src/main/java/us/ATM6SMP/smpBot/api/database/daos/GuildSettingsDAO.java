package us.ATM6SMP.smpBot.api.database.daos;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import us.ATM6SMP.smpBot.api.objects.settings.GuildSettings;

public class GuildSettingsDAO extends BasicDAO<GuildSettings, String> {
    public GuildSettingsDAO(Class<GuildSettings> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

}
