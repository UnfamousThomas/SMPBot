package us.ATM6SMP.smpBot.api.objects.user;

import com.google.api.client.util.ArrayMap;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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
    public void onReady(ReadyEvent event) {
        List<User> userList = SMPBot.getMongoManager().getUserDAO().find().asList();
        for (User user : userList) {
            users.add(user.getDiscordId(), user);
        }

        scheduleTimer();
    }

    private void scheduleTimer() {
        long delay = 300000L;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                users.forEach(((aLong, user) -> {
                    SMPBot.getMongoManager().getUserDAO().save(user);
                }));
            }
        }, delay);
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

        return user;
    }

    private void saveNewUser(User user) {
        users.add(user.getDiscordId(), user);
        SMPBot.getMongoManager().getUserDAO().save(user);
    }
}
