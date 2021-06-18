package us.unfamousthomas.bot.commands.test;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.bot.api.commands.Category;
import us.unfamousthomas.bot.api.commands.Command;
import us.unfamousthomas.bot.api.commands.CustomPermission;
import us.unfamousthomas.bot.api.objects.user.User;
import us.unfamousthomas.bot.api.objects.user.UserManager;

import java.util.List;

public class TestLevelExperience extends Command {
    public TestLevelExperience() {
        super("level");
        description = "test levels";
        usage = "!test level";
        permission = CustomPermission.ADMIN;
        category = Category.USEFUL;
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        User user = UserManager.getInstance().getUserByMember(m, m.getUser().getName());
        event.getChannel().sendMessage("Level: " + user.getLevel()).queue();
        event.getChannel().sendMessage(user.currentExperienceText()).queue();
    }
}
