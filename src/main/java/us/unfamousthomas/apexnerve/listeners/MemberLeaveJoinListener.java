package us.unfamousthomas.apexnerve.listeners;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import us.unfamousthomas.apexnerve.api.objects.settings.GuildSettingsManager;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamManager;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamObject;

public class MemberLeaveJoinListener extends ListenerAdapter {

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

        long textChannel = GuildSettingsManager.getInstance().getGuildSettings(event.getGuild().getIdLong()).getSystemTextChannel();

        TextChannel realText = event.getGuild().getTextChannelById(textChannel);

        if(realText == null) return;
        realText.sendMessage(event.getUser().getName() + " has left the discord server.").queue();

    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        long textChannel = GuildSettingsManager.getInstance().getGuildSettings(event.getGuild().getIdLong()).getSystemTextChannel();

        TextChannel realText = event.getGuild().getTextChannelById(textChannel);

        if(realText == null) return;
        realText.sendMessage(event.getUser().getName() + " has joined the discord server.").queue();

    }



}
