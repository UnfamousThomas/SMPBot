package us.unfamousthomas.apexnerve.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamManager;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamObject;

import java.util.List;

public class TeamLeaveCommand extends Command {
    public TeamLeaveCommand() {
        super("leave");
        aliases = alias("yeet");
        permission = CustomPermission.MEMBER;
        usage = "!team leave";
        description = "Used to leave your current team.";
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        leave(m, event.getTextChannel());
    }

    private void leave(Member m, TextChannel textChannel) {
        TeamObject leaderOf = TeamManager.getInstance().getTeamLeaderOf(m);
        if(leaderOf != null) {
            textChannel.sendMessage("You cannot leave your team as a leader. Please choose a new leader OR do !team delete.").queue();
            return;
        }
        TeamObject teamObject = TeamManager.getInstance().getTeamMemberOf(m);

        if(teamObject == null) {
            textChannel.sendMessage("Could not find you in any team!").queue();
            return;
        }

        TeamManager.getInstance().leaveTeam(m, teamObject);
    }

}
