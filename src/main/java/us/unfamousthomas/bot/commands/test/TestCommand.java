package us.unfamousthomas.bot.commands.test;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.bot.api.commands.Category;
import us.unfamousthomas.bot.api.commands.Command;
import us.unfamousthomas.bot.api.commands.CustomPermission;

import java.util.List;

public class TestCommand extends Command {
    public TestCommand() {
        super("test");
        category = Category.USEFUL;
        permission = CustomPermission.ADMIN;
        usage = "test";
        description = "A command to check bot response.";
        addSubcommands(
                new TestSubCommand(),
                new TestLevelExperience(),
                new TestEXPGiveCommand(),
                new TestLevelExperience(),
                new TestTotalEXPCommand()
        );
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        event.getChannel().sendMessage("Main test command").queue();
    }
}
