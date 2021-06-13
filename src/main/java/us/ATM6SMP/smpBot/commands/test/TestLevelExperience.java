package us.ATM6SMP.smpBot.commands.test;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Category;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;
import us.ATM6SMP.smpBot.api.objects.user.User;
import us.ATM6SMP.smpBot.api.objects.user.UserManager;

import java.util.List;

public class TestLevelExperience extends Command {
    public TestLevelExperience() {
        super("level");
        description = "test levels";
        usage = "!test level";
        permission = CustomPermission.DEV;
        category = Category.USEFUL;
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        User user = UserManager.getInstance().getUserByUser(m.getUser());
        event.getChannel().sendMessage("Level: " + user.getLevel()).queue();
        event.getChannel().sendMessage("Experience: " + user.getExperience() + " / " + user.getRequiredExperienceForNextLevel() + " (" + user.getPercentage() + "%" + ")").queue();
    }
}
