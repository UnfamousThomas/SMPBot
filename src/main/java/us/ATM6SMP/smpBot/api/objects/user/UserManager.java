package us.ATM6SMP.smpBot.api.objects.user;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import us.ATM6SMP.smpBot.SMPBot;
import us.ATM6SMP.smpBot.api.tasks.SaveUsersTaskTimer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class UserManager extends ListenerAdapter {
    private static HashMap<Long, User> users = new HashMap<>();

    private static UserManager instance;

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
        scheduleTasks();
    }

    private void scheduleTasks() {
        Timer userTask = new Timer();
        SaveUsersTaskTimer taskTimer = new SaveUsersTaskTimer(userTask);
        userTask.scheduleAtFixedRate(taskTimer, 100, 1000 * 60 * 20);
    }


    private User getUserByID(Long id) {
        User user;
        if(!checkIfUserExists(id)) {
            user = createNewUser(id);
        } else {
            user = users.get(id);
        }
        return user;
    }

    public boolean checkIfUserExists(Long id) {
        User user = users.get(id);

        if(user != null) {
            return true;
        } else {
            return false;
        }
    }

    public User getUserByMember(Member member, String discordName) {
        User botUser = getUserByID(member.getIdLong());
        botUser.checkName(discordName);

        return botUser;
    }

    public User getUserById(Long id) {
        return getUserByID(id);
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

    public void saveAll() {

        for (Map.Entry entry : users.entrySet()) {
            SMPBot.getMongoManager().getUserDAO().save((User) entry.getValue());
        }

    }
}
