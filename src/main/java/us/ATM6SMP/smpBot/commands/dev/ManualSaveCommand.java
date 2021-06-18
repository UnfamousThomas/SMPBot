package us.ATM6SMP.smpBot.commands.dev;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;
import us.ATM6SMP.smpBot.api.objects.settings.GuildSettingsManager;
import us.ATM6SMP.smpBot.api.objects.teams.TeamManager;
import us.ATM6SMP.smpBot.api.objects.user.UserManager;

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
