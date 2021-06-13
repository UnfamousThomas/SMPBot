package us.ATM6SMP.smpBot.commands.team;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;
import us.ATM6SMP.smpBot.api.objects.teams.TeamManager;

import java.util.List;

public class TeamDeleteCommand  extends Command {
    public TeamDeleteCommand() {
        super("delete");
        aliases = alias("d", "disband");

        permission = CustomPermission.MEMBER;

        usage = "!team delete";
        maxArgs = 0;
        minArgs = 0;
        description = "Used to delete the team as a team leader.";

    }


    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        TeamManager manager = TeamManager.getInstance();
        final boolean[] isInTeam = {false};

        manager.getTeams().forEach(teamObject -> {
            teamObject.getListOfMemberIds().forEach(member -> {
                if (member == m.getIdLong()) {
                    isInTeam[0] = true;
                }
            });
        });

        delete(isInTeam[0],event.getGuild(), event.getMember(), event.getTextChannel());



    }

    private void delete(boolean inTeam, Guild guild, Member m, TextChannel channel) {
        TeamManager manager = TeamManager.getInstance();

        if (inTeam) {
            final int[] index = new int[1];
            final boolean[] found = new boolean[1];
            manager.getTeams().forEach(team -> {
                if (team.getLeaderId() == m.getIdLong()) {
                    index[0] = manager.getTeams().indexOf(team);
                    found[0] = true;
                }
            });
            if (found[0]) {
                channel.sendMessage("Team: " + manager.getTeams().get(index[0]).getName() + " has been deleted.").queue();
                manager.deleteTeam(manager.getTeams().get(index[0]), guild);

            } else {
                channel.sendMessage("You are not a teamleader.").queue();
            }
        } else {
            channel.sendMessage("You are not in a team.").queue();
        }
    }
}
