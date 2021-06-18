package us.unfamousthomas.bot.commands.dev;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.bot.api.commands.Command;
import us.unfamousthomas.bot.api.commands.CustomPermission;
import us.unfamousthomas.bot.api.objects.settings.GuildSettingsManager;
import us.unfamousthomas.bot.api.objects.teams.TeamManager;
import us.unfamousthomas.bot.api.objects.user.UserManager;

import java.util.List;

public class ManualSaveCommand extends Command {
    public ManualSaveCommand() {
        super("manualsave");
        permission = CustomPermission.DEV;
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        GuildSettingsManager.getInstance().saveAll();
        UserManager.getInstance().saveAll();
        TeamManager.getInstance().saveAll();
        event.getTextChannel().sendMessage("Success!").queue();
    }
}
