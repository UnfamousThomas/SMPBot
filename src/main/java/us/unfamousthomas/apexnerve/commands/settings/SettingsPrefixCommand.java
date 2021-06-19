package us.unfamousthomas.apexnerve.commands.settings;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.Text;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;
import us.unfamousthomas.apexnerve.api.objects.settings.GuildSettingsManager;

import java.util.List;

public class SettingsPrefixCommand extends Command {
    public SettingsPrefixCommand() {
        super("prefix");
        minArgs = 0;
        maxArgs = 1;
        permission = CustomPermission.ADMIN;
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        if(args.size() == 0) {
            event.getTextChannel().sendMessage("Prefix: " + GuildSettingsManager.getInstance().getGuildSettings(m.getGuild().getIdLong()).getPrefix()).queue();
        } else {
            if(args.size() != 1) {
                event.getTextChannel().sendMessage(Text.INVALID_USAGE.getMessage()).queue();
                return;
            }

            String prefix = args.get(0);

            GuildSettingsManager.getInstance().getGuildSettings(m.getGuild().getIdLong()).setPrefix(prefix);

            event.getTextChannel().sendMessage("Prefix set to: " + prefix).queue();
        }
    }
}
