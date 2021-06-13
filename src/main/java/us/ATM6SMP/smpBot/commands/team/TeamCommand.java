package us.ATM6SMP.smpBot.commands.team;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.Category;
import us.ATM6SMP.smpBot.api.Command;
import us.ATM6SMP.smpBot.api.CustomPermission;
import us.ATM6SMP.smpBot.teams.TeamManager;
import us.ATM6SMP.smpBot.teams.TeamObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TeamCommand extends Command {
    public TeamCommand() {
        super("team");
        category = Category.TEAM;
        permission = CustomPermission.MEMBER;
        usage = "!team";
        description = "Used to access team commands (without any aliases will give you info about current team)";


        addSubcommands(
                new TeamCreateCommand(),
                new TeamDeleteCommand(),
                new TeamInviteCommand(),
                new TeamJoinCommand()
        );

    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        final TeamObject[] teamMemberOf = new TeamObject[1];
        TeamManager.getInstance().getTeams().forEach(team -> {
            if (team.getListOfMemberIds().contains(m.getIdLong())) {
                teamMemberOf[0] = team;
            }
        });
        if (teamMemberOf[0] == null) {
            event.getTextChannel().sendMessage("Not member of any team").queue();
        } else {
            sendEmbed(teamMemberOf[0], event.getGuild(), event.getTextChannel());
        }
    }

    private void sendEmbed(TeamObject team, Guild guild, TextChannel channel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setColor(team.getColor())
                .setTitle("Team Info")
                .setFooter("ATM6 SMP")
                .addField("Team name", team.getName(), true);

        StringBuilder builder = new StringBuilder();
        int total = team.getListOfMemberIds().size();
        final int[] i = {0};
        team.getListOfMemberIds().forEach(memberid -> {
            guild.retrieveMemberById(memberid).queue(member -> {
                if(memberid.equals(team.getLeaderId())) {
                    builder.append(member.getUser().getName());
                    builder.append(" (Leader)");
                } else {
                    builder.append(member.getUser().getName());
                }
                builder.append("\n");
                i[0] = i[0] + 1;
                if (i[0] == total) {
                    embedBuilder.addField("Members", builder.toString(), false);
                    channel.sendMessage(embedBuilder.build()).queue();
                }
            });
        });


    }
}
