package us.unfamousthomas.bot.api.database.daos;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import us.unfamousthomas.bot.api.objects.teams.TeamObject;

public class TeamDAO extends BasicDAO<TeamObject, String> {
    public TeamDAO(Class<TeamObject> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

}
