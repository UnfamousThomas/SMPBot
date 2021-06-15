package us.ATM6SMP.smpBot.api.objects.user;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import us.ATM6SMP.smpBot.SMPBot;
import us.ATM6SMP.smpBot.api.tasks.SaveUsersTaskTimer;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class UserManager extends ListenerAdapter {
    private static HashMap<Long, User> users = new HashMap<>();

    public static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<User> userList = SMPBot.getMongoManager().getUserDAO().find().asList();
        for (User user : userList) {
            users.put(user.getDiscordId(), user);
        }
        scheduleTask();
    }

    private void scheduleTask() {
        Timer timer = new Timer();
        SaveUsersTaskTimer taskTimer = new SaveUsersTaskTimer(timer);
        timer.scheduleAtFixedRate(taskTimer, 100, 1000);
    }

    public User getUserByID(Long id) {
        User user;
        if(!checkIfUserExists(id)) {
            user = createNewUser(id);
        } else {
            user = users.get(id);
        }
        return user;
    }

    private boolean checkIfUserExists(Long id) {
        User user = users.get(id);

        if(user != null) {
            return true;
        } else {
            return false;
        }
    }

    public User getUserByUser(net.dv8tion.jda.api.entities.User user) {
        return getUserByID(user.getIdLong());
    }

    public User createNewUser(Long id) {
        User user = new User();
        user.setDiscordId(id);
        return saveNewUser(user);
    }

    private User saveNewUser(User user) {
        SMPBot.getMongoManager().getUserDAO().save(user);
        users.put(user.getDiscordId(), user);

        return user;
    }

    public HashMap<Long, User> getUsers() {
        return users;
    }
}
