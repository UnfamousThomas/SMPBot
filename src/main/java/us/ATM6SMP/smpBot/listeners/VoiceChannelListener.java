package us.ATM6SMP.smpBot.listeners;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import us.ATM6SMP.smpBot.api.LevelUtils;
import us.ATM6SMP.smpBot.api.objects.user.UserManager;
import us.ATM6SMP.smpBot.api.timetracking.TimeTrackingManager;

public class VoiceChannelListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        if(event.getMember().getUser().isBot()) return;

        TimeTrackingManager.getInstance().registerTimeTarget(event.getMember().getUser().getIdLong());
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if(event.getMember().getUser().isBot()) return;

        double expAdded = LevelUtils.getExperienceFromVC(TimeTrackingManager.getInstance().getTimeTracking(event.getMember()).getTimeJoinedVC());
        UserManager.getInstance().getUserByID(event.getMember().getUser().getIdLong()).giveExperience(expAdded);

        TimeTrackingManager.getInstance().removeTimeTracking(event.getMember().getIdLong());
    }
}
