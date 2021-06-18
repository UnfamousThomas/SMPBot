package us.unfamousthomas.apexnerve.commands.team;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamManager;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamObject;

import java.util.List;

public class TeamDeleteCommand  extends Command {
    public TeamDeleteCommand() {
        super("delete");
        aliases = alias("d", "disband");

        permission = CustomPermission.MEMBER;

        usage = "!team delete";
        maxArgs = 0;
        minArgs = 0;
        description = "Used to delete the team as a team leader.";

    }


    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {

        boolean isInTeam = false;

        TeamObject teamObject = TeamManager.getInstance().getTeamMemberOf(m);
        if(teamObject != null) {
            isInTeam = true;
        }

        delete(isInTeam,event.getGuild(), event.getMember(), event.getTextChannel());



    }

    private void delete(boolean inTeam, Guild guild, Member m, TextChannel channel) {
        TeamManager manager = TeamManager.getInstance();

        if (inTeam) {

            TeamObject teamObject = TeamManager.getInstance().getTeamLeaderOf(m);

            if (teamObject != null) {
                channel.sendMessage("Team: " + teamObject.getName() + " has been deleted.").queue();
                manager.deleteTeam(teamObject, guild);

            } else {
                channel.sendMessage("You are not a teamleader.").queue();
            }
        } else {
            channel.sendMessage("You are not in a team.").queue();
        }
    }
}
