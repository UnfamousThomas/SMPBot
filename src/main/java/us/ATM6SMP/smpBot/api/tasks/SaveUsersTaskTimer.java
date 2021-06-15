package us.ATM6SMP.smpBot.api.tasks;

import us.ATM6SMP.smpBot.SMPBot;
import us.ATM6SMP.smpBot.api.objects.user.User;
import us.ATM6SMP.smpBot.api.objects.user.UserManager;

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
            SMPBot.getMongoManager().getUserDAO().save(entry.getValue());
        }

    }
    @Override
    public void run() {
        taskRun();
    }
}
