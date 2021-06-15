package us.ATM6SMP.smpBot.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;
import us.ATM6SMP.smpBot.api.objects.teams.InviteObject;
import us.ATM6SMP.smpBot.api.objects.teams.TeamManager;
import us.ATM6SMP.smpBot.api.objects.teams.TeamObject;

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
        final TeamObject[] teamToLeave = {null};
        TeamManager.getInstance().getTeams().forEach(teamObject -> {
            if(teamObject.getLeaderId() == m.getUser().getIdLong()) {
                textChannel.sendMessage("You cannot leave your team as a leader. Please choose a new leader OR do !team delete.").queue();
                return;
            }

            if(teamObject.getListOfMemberIds().contains(m.getUser().getIdLong())) {
                teamToLeave[0] = teamObject;
            }
        });

        if(teamToLeave[0] == null) {
            textChannel.sendMessage("Could not find you in any team!").queue();
            return;
        }

        TeamManager.getInstance().leaveTeam(m, teamToLeave[0]);
    }

}
