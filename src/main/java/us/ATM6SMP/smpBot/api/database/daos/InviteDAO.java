package us.ATM6SMP.smpBot.api.database.daos;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import us.ATM6SMP.smpBot.teams.InviteObject;
import us.ATM6SMP.smpBot.teams.TeamObject;

public class InviteDAO extends BasicDAO<InviteObject, String> {
    public InviteDAO(Class<InviteObject> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

}
