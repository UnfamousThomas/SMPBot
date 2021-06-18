package us.unfamousthomas.bot.api.database.daos;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import us.unfamousthomas.bot.api.objects.settings.GuildSettings;

public class GuildSettingsDAO extends BasicDAO<GuildSettings, String> {
    public GuildSettingsDAO(Class<GuildSettings> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

}
