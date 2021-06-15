package us.ATM6SMP.smpBot.api.commands;

import com.google.common.collect.Maps;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.DataHandler;

import java.util.List;
import java.util.Map;

public abstract class Command {
    final String name;
    public final Map<String, Command> subcommands =Maps.newHashMap();
    public String capitalizedName;
    public String usage;
    public int minArgs = 0;
    public int maxArgs = Integer.MAX_VALUE;
    protected String[] aliases = {};
    public CustomPermission permission;
    public String description = "No desc set.";
    public Category category;

    private DataHandler dataHandler = DataHandler.getInstance();

    public Command(String name) {
        this.name = name;
    }

    public void execute(MessageReceivedEvent event, List<String> args) {
        if (!(event.getChannel().getType().isGuild())) {
            event.getChannel().sendMessage("Has to be in a guild channel.").queue();
            return;
        }

        if(event.getAuthor().isBot()) return;


        if (args.size() > 0) {
            Command subcommand = subcommands.get(args.get(0).toLowerCase());
            if (subcommand != null) {
                args.remove(0);
                subcommand.execute(event, args);
                return;
            }
        }

        if (args.size() < minArgs || args.size() > maxArgs) {
            event.getChannel().sendMessage("Invalid usage. Please use .help.").queue();
            return;
        }

        TextChannel textChannel = event.getTextChannel();
        if (permission == CustomPermission.DEV || permission == CustomPermission.BOTHELPER) {
            if (permission == CustomPermission.DEV) {
                if (!dataHandler.getDev().contains(event.getAuthor().getId())) {
                    sendPermissionMessage(textChannel);
                    return;
                }
            } else {
                if (dataHandler.getBotHelperList().contains(event.getAuthor().getIdLong()) && !(dataHandler.getDev().contains(event.getAuthor().getId()))) {
                    sendPermissionMessage(textChannel);
                    return;
                }
            }
        }

        switch (permission) {
            case ADMIN:
                if (!(event.getMember().getPermissions().contains(Permission.valueOf(CustomPermission.ADMIN.perm)))) {
                    sendPermissionMessage(textChannel);
                    return;
                }
                break;
            case MODERATOR:
                if (!(event.getMember().getPermissions().contains(Permission.valueOf(CustomPermission.MODERATOR.perm)))) {
                    sendPermissionMessage(textChannel);
                    return;
                }
                break;
            case MEMBER:
                if (!(event.getMember().getPermissions().contains(Permission.valueOf(CustomPermission.MEMBER.perm)))) {
                    sendPermissionMessage(textChannel);
                    return;
                }
                break;
        }
        run(event.getMember(), args, event);
    }

    protected void addSubcommands(Command... commands) {
        for (Command command : commands) {
            subcommands.put(command.name, command);

            for (String alias : command.aliases)
                subcommands.put(alias, command);
        }
    }

    public String[] alias(String... aliases) {
        return aliases;
    }

    private void sendPermissionMessage(TextChannel tc) {
        tc.sendMessage("You do not have sufficient permissions for that command.").queue();
    }

    public abstract void run(Member m, List<String> args, MessageReceivedEvent event);
}
