package us.unfamousthomas.bot.listeners;

import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import us.unfamousthomas.bot.api.objects.teams.TeamManager;
import us.unfamousthomas.bot.api.objects.teams.TeamObject;

public class GuildLeaveJoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        TeamObject team = TeamManager.getInstance().getTeamMemberOf(event.getMember());

        if(team != null) {
            team.removeMember(event.getMember().getIdLong());
            if(team.getLeaderId() == event.getMember().getIdLong()) {
                if(team.getListOfMemberIds().size() > 0) {
                    long id = team.getListOfMemberIds().get(0);
                    team.setLeaderId(id);
                    event.getGuild().retrieveMemberById(id).queue(member -> {
                        event.getMember().getGuild().getTextChannelById(team.getTeamChatChannelId()).sendMessage(member.getUser().getName() + " is the new leader.").queue();
                    });
                }
            }
            event.getMember().getGuild().getTextChannelById(team.getTeamChatChannelId()).sendMessage(event.getMember().getUser().getName() + " has left the discord and has thus been removed from the members list.").queue();
        }

    }


}
