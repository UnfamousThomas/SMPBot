package us.ATM6SMP.smpBot.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;
import us.ATM6SMP.smpBot.api.objects.teams.TeamManager;
import us.ATM6SMP.smpBot.api.objects.teams.TeamObject;

import java.util.List;

public class TeamRenameCommand extends Command {
    public TeamRenameCommand() {
        super("rename");
        aliases = alias("name");
        permission = CustomPermission.MEMBER;
        minArgs = 1;
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        String newName = String.join(" ", args);

        TeamObject team = TeamManager.getInstance().getTeamLeaderOf(m);

        if(team != null) {
            TeamManager.getInstance().renameTeam(newName, team, m);
        } else {
            event.getTextChannel().sendMessage("Could not find a team with you as leader.").queue();
        }
    }
}