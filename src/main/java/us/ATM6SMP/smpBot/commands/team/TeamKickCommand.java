package us.ATM6SMP.smpBot.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.objects.teams.TeamManager;
import us.ATM6SMP.smpBot.api.objects.teams.TeamObject;

import java.util.List;

public class TeamKickCommand extends Command {
    public TeamKickCommand() {
        super("kick");
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        if(!(event.getMessage().getMentionedMembers().size() == 1)) {
            event.getTextChannel().sendMessage("You need to mention the user you wish to kick from your team.").queue();
            return;
        }
        TeamObject toRemove = null;
        for (TeamObject team : TeamManager.getInstance().getTeams()) {
            for (Long memberId : team.getListOfMemberIds()) {
                if(memberId.equals(m.getIdLong())) {
                    toRemove = team;
                }
            }

            if(toRemove != null) {
                TeamManager.getInstance().kickTeam(m, toRemove);
            } else {
                event.getTextChannel().sendMessage("Team not found").queue();
            }
        }

    }
}
