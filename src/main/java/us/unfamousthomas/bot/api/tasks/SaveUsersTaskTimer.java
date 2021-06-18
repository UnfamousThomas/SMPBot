package us.unfamousthomas.bot.api.tasks;

import us.unfamousthomas.bot.api.database.MongoManager;
import us.unfamousthomas.bot.api.objects.user.User;
import us.unfamousthomas.bot.api.objects.user.UserManager;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SaveUsersTaskTimer extends TimerTask {
    Timer timer;

    public SaveUsersTaskTimer(Timer timer) {
        this.timer = timer;
    }

    public void taskRun() {
        for (Map.Entry<Long, User> entry : UserManager.getInstance().getUsers().entrySet()) {
            MongoManager.getInstance().getUserDAO().save(entry.getValue());
        }

    }
    @Override
    public void run() {
        taskRun();
    }
}
