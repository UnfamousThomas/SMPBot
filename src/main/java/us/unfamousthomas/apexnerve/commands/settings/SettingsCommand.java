package us.unfamousthomas.apexnerve.commands.settings;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;

import java.util.List;

public class SettingsCommand extends Command {
    public SettingsCommand() {
        super("settings");
        permission = CustomPermission.ADMIN;
        addSubcommands(
                new SettingsSystemMessagesCommand(),
                new SettingsPrefixCommand()
        );
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage("You cannot currently view settings this way. Please use !settings [subcommand]").queue();
    }


}
