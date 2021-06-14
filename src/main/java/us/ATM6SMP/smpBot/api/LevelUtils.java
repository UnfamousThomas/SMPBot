package us.ATM6SMP.smpBot.api;

import java.util.concurrent.TimeUnit;

public class LevelUtils {

    public static double experienceFormula(int level) {
        return level * level * 100;
    }

    public static double getExperienceFromVC(long timeJoined) {
        long timespent = System.currentTimeMillis() - timeJoined;

        double minutes = TimeUnit.MILLISECONDS.toMinutes(timespent);

        //For every minute spent in a NON-AFK vc channel, user should receive 10 experience

        return minutes * 10;
    }

    public static double getExperienceFromMessages(int amountOfMessages) {

        //For every message user receives around 0.01 experience, that means that 100 messages = 1 exp

        return (amountOfMessages * 0.01);
    }
}
