package us.ATM6SMP.smpBot.commands.test;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Category;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;

import java.util.List;

public class TestSubCommand extends Command {
    public TestSubCommand() {
        super("sub");
        category = Category.USEFUL;
        permission = CustomPermission.DEV;
        aliases = alias("s");
        usage = "!test sub";
        description = "Used to check subcommand responsiveness.";
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage("test sub").queue();
    }
}
