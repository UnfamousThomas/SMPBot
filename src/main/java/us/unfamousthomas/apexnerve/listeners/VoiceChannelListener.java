package us.unfamousthomas.apexnerve.listeners;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import us.unfamousthomas.apexnerve.api.Utils;
import us.unfamousthomas.apexnerve.api.objects.user.UserManager;
import us.unfamousthomas.apexnerve.api.timetracking.TimeTrackingManager;

public class VoiceChannelListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        if (event.getMember().getUser().isBot()) return;

        TimeTrackingManager.getInstance().registerTimeTarget(event.getMember().getUser().getIdLong());
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if (event.getMember().getUser().isBot()) return;

        if (TimeTrackingManager.getInstance().getTimeTracking(event.getMember()) != null) {
            double expAdded = Utils.getExperienceFromVC(TimeTrackingManager.getInstance().getTimeTracking(event.getMember()).getTimeJoinedVC());
            UserManager.getInstance().getUserByMember(event.getMember(), event.getMember().getUser().getName()).giveExperience(expAdded);

            TimeTrackingManager.getInstance().removeTimeTracking(event.getMember().getIdLong());
        }
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        if (event.getChannelLeft().getGuild().getAfkChannel() == event.getChannelLeft()) {
            //Left AFK channel, start time tracking:
            if (event.getMember().getUser().isBot()) return;

            TimeTrackingManager.getInstance().registerTimeTarget(event.getMember().getUser().getIdLong());
        } else if (event.getChannelJoined().getGuild().getAfkChannel() == event.getChannelJoined()) {
            //Joined AFK channel, stop time tracking:
            if (event.getMember().getUser().isBot()) return;

            if (TimeTrackingManager.getInstance().getTimeTracking(event.getMember()) != null) {
                double expAdded = Utils.getExperienceFromVC(TimeTrackingManager.getInstance().getTimeTracking(event.getMember()).getTimeJoinedVC());
                UserManager.getInstance().getUserByMember(event.getMember(), event.getMember().getUser().getName()).giveExperience(expAdded);

                TimeTrackingManager.getInstance().removeTimeTracking(event.getMember().getIdLong());
            }
        }
    }
}
