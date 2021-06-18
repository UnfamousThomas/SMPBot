package us.unfamousthomas.bot.api.timetracking;

public class TimeTargetObject {

    public TimeTargetObject(long userID) {
        this.userID = userID;
        this.timeJoinedVC = System.currentTimeMillis();
        TimeTrackingManager.getInstance().addTimeTarget(this);
    }
    public long userID;
    public long timeJoinedVC;

    public long getTimeJoinedVC() {
        return timeJoinedVC;
    }

    public long getUserID() {
        return userID;
    }
}
