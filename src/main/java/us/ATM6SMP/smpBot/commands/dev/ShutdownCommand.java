package us.ATM6SMP.smpBot.commands.dev;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;

import java.util.List;

public class ShutdownCommand extends Command {
    public ShutdownCommand() {
        super("shutdown");
        aliases = alias("stop");
        permission = CustomPermission.DEV;
        usage = "!shutdown";
        description = "Naughty boi command.";
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        event.getJDA().shutdown();
    }
}
