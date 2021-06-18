package us.unfamousthomas.apexnerve.api.database.daos;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamObject;

public class TeamDAO extends BasicDAO<TeamObject, String> {
    public TeamDAO(Class<TeamObject> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

}
