package us.ATM6SMP.smpBot.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;
import us.ATM6SMP.smpBot.teams.TeamManager;
import us.ATM6SMP.smpBot.teams.TeamObject;

import java.util.List;

public class TeamInviteCommand extends Command {
    public TeamInviteCommand() {
        super("invite");
        aliases = alias("add", "i");
        permission = CustomPermission.MEMBER;
        usage = "!team invite [TAGGED USER]";
        minArgs = 1;
        maxArgs = 1;
        description = "Used to send an invite";
    }

    TeamManager manager = TeamManager.getInstance();

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        invite(m, event.getTextChannel(), event.getMessage());
    }

    private void invite(Member m, TextChannel textChannel, Message message) {
        List<Member> members = message.getMentionedMembers();
        if(members.size() == 0) {
            textChannel.sendMessage("You did not tag any user.").queue();
            return;
        } else if(members.size() != 1) {
            textChannel.sendMessage("Please invite one person at a time.").queue();
            return;
        } else if(members.contains(m)) {
            textChannel.sendMessage("You cannot invite yourself").queue();
            return;
        }

        final int[] index = new int[1];
        final boolean[] found = new boolean[1];

        Member memberToInvite = members.get(0);
        manager.getTeams().forEach(team -> {
            if (team.getLeaderId() == m.getIdLong()) {
                index[0] = manager.getTeams().indexOf(team);
                found[0] = true;
            }
        });

        final TeamObject[] membersTeam = {null};

        manager.getTeams().forEach(team -> {
            if(team.getListOfMemberIds().contains(members.get(0).getUser().getIdLong())) {
                membersTeam[0] = team;
            }
        });
        final TeamObject[] leaderTeam = {null};

        manager.getTeams().forEach(team -> {
            if(team.getLeaderId() == m.getIdLong()) {
                leaderTeam[0] = team;
            }
        });
        if(membersTeam[0] == leaderTeam[0]) {
            textChannel.sendMessage("Cannot invite a member already in your team").queue();
            return;
        }

        if(found[0]) {
            manager.inviteToTeam(manager.getTeams().get(index[0]), memberToInvite);
        } else {
            textChannel.sendMessage("Could not find you as a leader in a team!").queue();
        }

    }

}
