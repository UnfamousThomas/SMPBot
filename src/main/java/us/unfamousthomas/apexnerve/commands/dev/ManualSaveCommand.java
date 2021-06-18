package us.unfamousthomas.apexnerve.commands.dev;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;
import us.unfamousthomas.apexnerve.api.objects.settings.GuildSettingsManager;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamManager;
import us.unfamousthomas.apexnerve.api.objects.user.UserManager;

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
