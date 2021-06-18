package us.unfamousthomas.apexnerve.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;
import us.unfamousthomas.apexnerve.api.objects.settings.GuildSettingsManager;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamManager;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamObject;

import java.util.List;

public class TeamInviteCommand extends Command {
    public TeamInviteCommand() {
        super("invite");
        aliases = alias("add", "i");
        permission = CustomPermission.MEMBER;
        usage = "!team invite [TAGGED USER]";
        minArgs = 1;
        maxArgs = 1;
        description = "Used to send an invite";
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        invite(m, event.getTextChannel(), event.getMessage());
    }

    private void invite(Member m, TextChannel textChannel, Message message) {
        TeamObject team  = TeamManager.getInstance().getTeamLeaderOf(m);


        if(team == null) {
            textChannel.sendMessage("Could not find you as leader of team").queue();
            return;
        }

        if(team.getListOfMemberIds().size() == GuildSettingsManager.getInstance().getGuildSettings(m.getGuild().getIdLong()).getMaxTeam()) {
            textChannel.sendMessage("Max member count already reached").queue();
            return;
        }

        if(message.getMentionedMembers().size() != 1) {
            textChannel.sendMessage("Mentioned members size is not 1.").queue();
            return;
        }

        Member target = message.getMentionedMembers().get(0);
        TeamObject targetTeam = TeamManager.getInstance().getTeamMemberOf(target);

        if(targetTeam != null) {
            textChannel.sendMessage("User is already part of a team").queue();
            return;
        }

        TeamManager.getInstance().inviteToTeam(team, target);

    }

}
