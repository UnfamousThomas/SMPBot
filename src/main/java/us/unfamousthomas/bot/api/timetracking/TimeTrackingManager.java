package us.unfamousthomas.bot.api.timetracking;

import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.Map;

public class TimeTrackingManager {
    Map<Long, TimeTargetObject> timeTargets = new HashMap<>();
    public static TimeTrackingManager instance;

    public void registerTimeTarget(long userID) {
        TimeTargetObject timeTarget = new TimeTargetObject(userID);
    }

    public void addTimeTarget(TimeTargetObject timeTarget) {
        timeTargets.put(timeTarget.getUserID(), timeTarget);
    }

    public TimeTargetObject getTimeTracking(Member member) {
        return timeTargets.get(member.getUser().getIdLong());
    }

    public static TimeTrackingManager getInstance() {
        if(instance == null) {
            instance = new TimeTrackingManager();
        }
        return instance;
    }

    public void removeTimeTracking(long id) {
        timeTargets.remove(id);
    }
}
