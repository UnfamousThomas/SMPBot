package us.unfamousthomas.apexnerve.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.Text;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamManager;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamObject;

import java.util.List;

public class TeamLeaderCommand extends Command {
    public TeamLeaderCommand() {
        super("leader");
        minArgs = 0;
        maxArgs = 1;
        permission = CustomPermission.MEMBER;
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        if(args.size() == 0) {
            TeamObject teamObject = TeamManager.getInstance().getTeamMemberOf(m);
            if(teamObject == null) {
                event.getTextChannel().sendMessage(Text.TEAM_NOTFOUND.getMessage()).queue();
                return;
            }
            event.getGuild().retrieveMemberById(teamObject.getLeaderId()).queue(leader -> {
                event.getTextChannel().sendMessage(Text.LEADER_OF_TEAM.getReplaced("%name", leader.getUser().getName())).queue();
            });
        } else if(args.size() == 1) {
            TeamObject teamObject = TeamManager.getInstance().getTeamLeaderOf(m);

            if(teamObject == null) {
                event.getTextChannel().sendMessage(Text.YOUR_TEAM_LEADER_NOTFOUND.getMessage()).queue();
                return;
            }

            if(event.getMessage().getMentionedMembers().size() == 1) {
                Member target = event.getMessage().getMentionedMembers().get(0);
                TeamObject targetTeam = TeamManager.getInstance().getTeamMemberOf(target);

                if(targetTeam != teamObject) {
                    event.getTextChannel().sendMessage(Text.USER_NOT_IN_YOUR_TEAM.getMessage()).queue();
                    return;
                }

                TeamManager.getInstance().setNewLeader(m, target, teamObject);
                event.getTextChannel().sendMessage(Text.SUCCESSFUL.getMessage()).queue();
            } else {
                event.getTextChannel().sendMessage(Text.MENTION_EXACTLY_ONE.getMessage()).queue();
            }
        }
    }
}
