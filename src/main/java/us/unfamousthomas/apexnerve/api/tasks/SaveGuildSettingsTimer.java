package us.unfamousthomas.apexnerve.api.tasks;

import us.unfamousthomas.apexnerve.api.objects.settings.GuildSettingsManager;

import java.util.Timer;
import java.util.TimerTask;

public class SaveGuildSettingsTimer extends TimerTask {
    Timer timer;

    public SaveGuildSettingsTimer(Timer timer) {
        this.timer = timer;
    }

    public void taskRun() {
        GuildSettingsManager.getInstance().saveAll();

    }
    @Override
    public void run() {
        taskRun();
    }
}
