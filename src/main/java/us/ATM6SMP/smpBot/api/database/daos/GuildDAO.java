package us.ATM6SMP.smpBot.api.database.daos;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class GuildDAO extends BasicDAO<Guild, String> {
    public GuildDAO(Class<Guild> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

}
