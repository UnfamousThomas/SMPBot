package us.ATM6SMP.smpBot.api.tasks;

import us.ATM6SMP.smpBot.SMPBot;
import us.ATM6SMP.smpBot.api.ScoreboardManager;
import us.ATM6SMP.smpBot.api.objects.user.User;
import us.ATM6SMP.smpBot.api.objects.user.UserManager;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateScoreboardTimer extends TimerTask {
    Timer timer;

    public UpdateScoreboardTimer(Timer timer) {
        this.timer = timer;
    }

    public void taskRun() {
        ScoreboardManager.getInstance().updateScoreboard();

    }
    @Override
    public void run() {
        taskRun();
    }
}
