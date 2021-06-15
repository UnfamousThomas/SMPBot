package us.ATM6SMP.smpBot.api.database.daos;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import us.ATM6SMP.smpBot.api.objects.Guild;
import us.ATM6SMP.smpBot.api.objects.user.User;

public class GuildDAO extends BasicDAO<Guild, String> {
    public GuildDAO(Class<Guild> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

}
