package us.unfamousthomas.bot.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.bot.api.commands.Command;
import us.unfamousthomas.bot.api.commands.CustomPermission;
import us.unfamousthomas.bot.api.objects.teams.TeamManager;
import us.unfamousthomas.bot.api.objects.teams.TeamObject;

import java.util.List;

public class TeamKickCommand extends Command {
    public TeamKickCommand() {
        super("kick");
        permission = CustomPermission.MEMBER;
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        if(!(event.getMessage().getMentionedMembers().size() == 1)) {
            event.getTextChannel().sendMessage("You need to mention the user you wish to kick from your team.").queue();
            return;
        }

        TeamObject teamToKickFrom = TeamManager.getInstance().getTeamLeaderOf(m);

        if(teamToKickFrom == null) {
            event.getTextChannel().sendMessage("Could not find team bla bla bla").queue();
            return;
        }
        Member target = event.getMessage().getMentionedMembers().get(0);

        TeamManager.getInstance().kickTeam(target, teamToKickFrom);

    }
}
