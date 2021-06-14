package us.ATM6SMP.smpBot.api.objects.user;

import com.google.api.client.util.ArrayMap;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import us.ATM6SMP.smpBot.SMPBot;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UserManager extends ListenerAdapter {
    public ArrayMap<Long, User> users = new ArrayMap<>();

    public static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Ready!");
        List<User> userList = SMPBot.getMongoManager().getUserDAO().find().asList();
        for (User user : userList) {
            System.out.println("Saving: " + user.getDiscordId());
            users.add(user.getDiscordId(), user);
        }

        users.forEach((aLong, user) -> {
            System.out.println("Found: " + aLong);
        });

        scheduleTimer();

    }

    public void scheduleTimer() {
        System.out.println("While scheduling: " + users.size());
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateData();
            }
        }, 1000, 10000L);
    }

    private void updateData() {
        System.out.println("Initializing save.");
        System.out.println(users.size() + " users found in list.");
        users.forEach(((aLong, user) -> {
            System.out.println("Saving: " + user.getDiscordId());
            SMPBot.getMongoManager().getUserDAO().save(user);
            System.out.println("Save succesful!");
        }));
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        System.out.println("Bye!");
        users.forEach(((aLong, user) -> {
            SMPBot.getMongoManager().getUserDAO().save(user);
        }));
    }


    public User getUserByID(Long id) {
        User user = users.get(id);

        if(user == null) {
            user = createNewUser(id);
        }
        return user;
    }

    public User getUserByUser(net.dv8tion.jda.api.entities.User user) {
        return getUserByID(user.getIdLong());
    }

    public User createNewUser(Long id) {
        User user = new User();
        user.setDiscordId(id);
        saveNewUser(user);
        System.out.println("Saving new user: " + user.getDiscordId());
        return user;
    }

    private void saveNewUser(User user) {
        users.add(user.getDiscordId(), user);
        SMPBot.getMongoManager().getUserDAO().save(user);
    }
}
