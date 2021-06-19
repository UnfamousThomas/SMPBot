package us.unfamousthomas.apexnerve.commands.team;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.Text;
import us.unfamousthomas.apexnerve.api.commands.Category;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamManager;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamObject;

import java.time.Year;
import java.util.List;

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
                new TeamJoinCommand(),
                new TeamDenyCommand(),
                new TeamLeaveCommand(),
                new TeamKickCommand(),
                new TeamRenameCommand(),
                new TeamRecolorCommand(),
                new TeamLeaderCommand()
        );

    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        TeamObject teamMemberOf = TeamManager.getInstance().getTeamMemberOf(m);
        if (teamMemberOf == null) {
            event.getTextChannel().sendMessage(Text.YOUR_TEAM_NOTFOUND.getMessage()).queue();
        } else {
            sendEmbed(teamMemberOf, event.getGuild(), event.getTextChannel());
        }

    }

    private void sendEmbed(TeamObject team, Guild guild, TextChannel channel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setColor(team.getColor())
                .setTitle("Team Info")
                .setFooter(Text.BOT_FOOTER.getReplaced("%year", String.valueOf(Year.now().getValue())))
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
