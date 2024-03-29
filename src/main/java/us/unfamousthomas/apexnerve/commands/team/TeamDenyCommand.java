package us.unfamousthomas.apexnerve.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.Text;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;
import us.unfamousthomas.apexnerve.api.objects.teams.InviteObject;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamManager;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamObject;

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
            event.getTextChannel().sendMessage(Text.NUMBER_FORMAT_ERROR.getMessage()).queue();
        }
    }


    private void deny(Member m, MessageReceivedEvent e, Long id) {
        InviteObject invite = TeamManager.getInstance().getInviteFromGuildByLeaderId(id, m.getIdLong(), m.getGuild());

        if (invite == null) {
            e.getTextChannel().sendMessage(Text.INVITE_NOT_FOUND.getMessage()).queue();
            return;
        }

        if(!invite.isActive()) {
            e.getTextChannel().sendMessage(Text.INVITE_NOT_ACTIVE.getMessage()).queue();
            return;
        }

        TeamObject team = TeamManager.getInstance().getTeamLeaderOfById(id, m.getGuild());

        if (team == null) {
            e.getTextChannel().sendMessage(Text.TEAM_NOTFOUND.getMessage()).queue();
            return;
        }

        TeamManager.getInstance().denyInvite(m, team, invite);

    }
}
