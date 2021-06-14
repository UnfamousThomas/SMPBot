package us.ATM6SMP.smpBot.commands.test;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Category;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;
import us.ATM6SMP.smpBot.api.objects.user.UserManager;

import java.util.List;

public class TestEXPGiveCommand extends Command {
    public TestEXPGiveCommand() {
        super("exp-give");
        category = Category.DEV;
        permission = CustomPermission.DEV;
        usage = "No";
        description = "stop";
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        Long id = Long.parseLong(args.get(0));
        UserManager.getInstance().getUserByID(id).giveExperience(Double.parseDouble(args.get(1)));
        event.getChannel().sendMessage("Success!").queue();
    }
}
