package us.ATM6SMP.smpBot.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.Command;
import us.ATM6SMP.smpBot.api.CustomPermission;
import us.ATM6SMP.smpBot.teams.InviteObject;
import us.ATM6SMP.smpBot.teams.TeamManager;
import us.ATM6SMP.smpBot.teams.TeamObject;

import java.util.List;

public class TeamDenyCommand extends Command {
    public TeamDenyCommand() {
        super("deny");
        aliases = alias("refuse");
        description = "Allows you to refuse invitations.";
        usage = "!team deny";
        permission = CustomPermission.MEMBER;
        minArgs = 1;
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        try {
            Long id = Long.parseLong(args.get(0));
            deny(m, event, id);
        } catch (NumberFormatException ignore) {
            event.getTextChannel().sendMessage("Invalid ID.").queue();
        }
    }

    private void deny(Member m, MessageReceivedEvent e, Long id) {
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

        manager.denyInvite(m, object[0], inviteObject[0]);

    }
}
