package us.ATM6SMP.smpBot.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;
import us.ATM6SMP.smpBot.teams.InviteObject;
import us.ATM6SMP.smpBot.teams.TeamManager;
import us.ATM6SMP.smpBot.teams.TeamObject;

import java.util.List;

public class TeamJoinCommand extends Command {
    public TeamJoinCommand() {
        super("join");
        aliases = alias("accept");
        permission = CustomPermission.MEMBER;
        maxArgs = 1;
        usage = "!team join [ID]";
        description = "Used to accept an invite";
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        try {
            accept(m, event, Long.parseLong(args.get(0)));
        } catch (NumberFormatException ignored) {
            event.getTextChannel().sendMessage("Invalid id.").queue();
        }
    }

    private void accept(Member m, MessageReceivedEvent e, Long id) {
        TeamManager manager = TeamManager.getInstance();
        final InviteObject[] inviteObject = {null};
        manager.getInvites().forEach(invite -> {
            if(invite.getMemberSentTo() == m.getIdLong() && invite.getMemberSentFrom().equals(id)) {
                inviteObject[0] = invite;
            }
        });

        if(inviteObject[0] == null) {
            e.getTextChannel().sendMessage("Error finding invite.").queue();
            return;
        }

        final TeamObject[] object = {null};
        manager.getTeams().forEach(teamObject -> {
            if (teamObject.getLeaderId().equals(id)) {
                object[0] = teamObject;
            }
        });

        if (object[0] == null) {
            e.getTextChannel().sendMessage("Could not find team. Try again!").queue();
            return;
        }

        manager.joinTeam(m, object[0], inviteObject[0]);

    }
}
