package us.unfamousthomas.apexnerve.api.database.daos;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import us.unfamousthomas.apexnerve.api.objects.teams.InviteObject;

public class InviteDAO extends BasicDAO<InviteObject, String> {
    public InviteDAO(Class<InviteObject> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

}
