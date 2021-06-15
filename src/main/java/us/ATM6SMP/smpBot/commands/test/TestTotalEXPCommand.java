package us.ATM6SMP.smpBot.commands.test;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;

import java.util.List;

public class TestTotalEXPCommand extends Command {
    public TestTotalEXPCommand() {
        super("totalexp");
        minArgs = 1;
        maxArgs = 1;
        permission = CustomPermission.DEV;
        description = "secret";
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        int level = Integer.parseInt(args.get(0));

        int i = 0;

        int sum = 0;
        while(level <= i) {
            i = i + 1;
            sum = sum + (100 * level * level);
        }

        event.getChannel().sendMessage("EXP required: " + sum).queue();
    }
}
