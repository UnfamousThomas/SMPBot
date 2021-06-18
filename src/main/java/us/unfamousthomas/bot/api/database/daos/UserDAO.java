package us.unfamousthomas.bot.api.database.daos;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import us.unfamousthomas.bot.api.objects.user.User;

public class UserDAO extends BasicDAO<User, String> {
    public UserDAO(Class<User> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

}
