package us.ATM6SMP.smpBot.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;
import us.ATM6SMP.smpBot.api.objects.settings.GuildSettingsManager;
import us.ATM6SMP.smpBot.api.objects.teams.InviteObject;
import us.ATM6SMP.smpBot.api.objects.teams.TeamManager;
import us.ATM6SMP.smpBot.api.objects.teams.TeamObject;

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
            e.getTextChannel().sendMessage("Could not find invite.").queue();
            return;
        }

        if(!invite.isActive()) {
            e.getTextChannel().sendMessage("Invite is no longer active.").queue();
            return;
        }

        TeamObject team = TeamManager.getInstance().getTeamLeaderOfById(id, m.getGuild());

        if (team == null) {
            e.getTextChannel().sendMessage("Team for ID not found.").queue();
            return;
        }

        if (team.getListOfMemberIds().size() == GuildSettingsManager.getInstance().getGuildSettings(e.getGuild().getIdLong()).getMaxTeam()) {
            e.getTextChannel().sendMessage("Limit reached.").queue();
            return;
        }

        TeamManager.getInstance().joinTeam(m, team, invite);

    }
}
