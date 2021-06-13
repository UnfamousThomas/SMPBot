package us.ATM6SMP.smpBot.commands.test;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.Category;
import us.ATM6SMP.smpBot.api.Command;
import us.ATM6SMP.smpBot.api.CustomPermission;

import java.util.List;

public class TestCommand extends Command {
    public TestCommand() {
        super("test");
        category = Category.USEFUL;
        permission = CustomPermission.DEV;
        usage = "test";
        description = "A command to check bot response.";
        addSubcommands(
                new TestSubCommand()
        );
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        event.getChannel().sendMessage("Main test command").queue();
    }
}
