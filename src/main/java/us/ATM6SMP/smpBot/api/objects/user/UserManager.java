package us.ATM6SMP.smpBot.api.objects.user;

import com.google.api.client.util.ArrayMap;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import us.ATM6SMP.smpBot.SMPBot;
import us.ATM6SMP.smpBot.api.database.MongoManager;

import java.util.List;

public class UserManager {
    public ArrayMap<Long, User> users = new ArrayMap<>();

    public static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void startUpLoad(JDA jda) {
        List<User> userList = SMPBot.getMongoManager().getUserDAO().find().asList();
        for (User user : userList) {
            users.add(user.getDiscordId(), user);
        }

        //todo figure out load
        for (net.dv8tion.jda.api.entities.User user : jda.getUsers()) {
            if (!users.containsKey(user.getIdLong())) {
                User newUser = new User();
                newUser.setDiscordId(user.getIdLong());
                users.add(user.getIdLong(), newUser);
                System.out.println(user.getName());
            }

        }

    }

    public User getUserByID(Long id) {
        return users.get(id);
    }

    public User getUserByUser(net.dv8tion.jda.api.entities.User user) {
        return getUserByID(user.getIdLong());
    }
}
