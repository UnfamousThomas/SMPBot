package us.unfamousthomas.bot.commands.test;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.bot.api.commands.Category;
import us.unfamousthomas.bot.api.commands.Command;
import us.unfamousthomas.bot.api.commands.CustomPermission;

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
