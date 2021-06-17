package us.ATM6SMP.smpBot.commands.leaderboards;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;
import us.ATM6SMP.smpBot.api.leaderboard.GlobalLeaderboard;
import us.ATM6SMP.smpBot.api.leaderboard.LocalLeaderboard;

import java.util.List;

public class LeaderboardCommand extends Command {
    public LeaderboardCommand() {
        super("leaderboard");
        description = "Shows the leaderboard";
        permission = CustomPermission.MEMBER;
        usage = "!leaderboard (num)";
        minArgs = 0;
        maxArgs = 1;
        addSubcommands(
                new GlobalLeaderboardCommand()
        );
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        if(args.size() == 0) {
            event.getChannel().sendMessage(new LocalLeaderboard(m.getGuild()).getLeaderBoards(10)).queue();
        }

        if(args.size() == 1) {
            int num = getNumber(args.get(0));

            if(num > 0) {
                event.getChannel().sendMessage(new LocalLeaderboard(m.getGuild()).getLeaderBoards(num)).queue();
            } else {
                event.getChannel().sendMessage("Error with number").queue();
            }
        }
    }

    private int getNumber(String num) {
        int amount = 0;
        try {
            amount = Integer.parseInt(num);
            return amount;
        } catch (NumberFormatException ex) {
            amount = -5;
        }

        return amount;
    }
}
