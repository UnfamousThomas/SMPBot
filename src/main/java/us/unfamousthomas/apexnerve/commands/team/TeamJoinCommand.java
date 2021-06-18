package us.unfamousthomas.apexnerve.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.Text;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;
import us.unfamousthomas.apexnerve.api.objects.settings.GuildSettingsManager;
import us.unfamousthomas.apexnerve.api.objects.teams.InviteObject;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamManager;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamObject;

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

        if (team.getListOfMemberIds().size() == GuildSettingsManager.getInstance().getGuildSettings(e.getGuild().getIdLong()).getMaxTeam()) {
            e.getTextChannel().sendMessage(Text.MAX_MEMBERCOUNT.getMessage()).queue();
            return;
        }

        TeamManager.getInstance().joinTeam(m, team, invite);

    }
}
