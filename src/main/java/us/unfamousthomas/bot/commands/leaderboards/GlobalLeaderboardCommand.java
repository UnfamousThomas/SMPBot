package us.unfamousthomas.bot.commands.leaderboards;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.bot.api.commands.Command;
import us.unfamousthomas.bot.api.commands.CustomPermission;
import us.unfamousthomas.bot.api.leaderboard.GlobalLeaderboard;

import java.util.List;

public class GlobalLeaderboardCommand extends Command {
    public GlobalLeaderboardCommand() {
        super("global");
        description = "Shows the global leaderboard";
        aliases = alias("g");
        permission = CustomPermission.MEMBER;
        usage = "!leaderboard global (num)";
        minArgs = 0;
        maxArgs = 1;
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {

        if (args.size() == 0) {
            event.getChannel().sendMessage(new GlobalLeaderboard().getLeaderBoards(10)).queue();
        }

        if (args.size() == 1) {
            int num = getNumber(args.get(0));

            if (num > 0) {
                event.getChannel().sendMessage(new GlobalLeaderboard().getLeaderBoards(num)).queue();
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
