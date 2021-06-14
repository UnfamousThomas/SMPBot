package us.ATM6SMP.smpBot.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;

import java.util.List;

public class TeamLeaveCommand extends Command {
    public TeamLeaveCommand() {
        super("leave");
        aliases = alias("yeet");
        permission = CustomPermission.MEMBER;
        usage = "!team leave";
        description = "Used to leave your current team.";
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {

    }
}
