package us.unfamousthomas.apexnerve.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.Text;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamManager;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamObject;

import java.util.List;

public class TeamKickCommand extends Command {
    public TeamKickCommand() {
        super("kick");
        permission = CustomPermission.MEMBER;
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        if(event.getMessage().getMentionedMembers().size() != 1) {
            event.getTextChannel().sendMessage(Text.MENTION_EXACTLY_ONE.getMessage()).queue();
            return;
        }

        TeamObject teamToKickFrom = TeamManager.getInstance().getTeamLeaderOf(m);

        if(teamToKickFrom == null) {
            event.getTextChannel().sendMessage(Text.TEAM_NOTFOUND.getMessage()).queue();
            return;
        }
        Member target = event.getMessage().getMentionedMembers().get(0);

        TeamManager.getInstance().kickTeam(target, teamToKickFrom);

    }
}
