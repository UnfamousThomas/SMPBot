package us.ATM6SMP.smpBot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.Category;
import us.ATM6SMP.smpBot.api.Command;
import us.ATM6SMP.smpBot.api.CommandManager;
import us.ATM6SMP.smpBot.api.CustomPermission;

import java.util.List;
import java.util.Locale;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help");
        permission = CustomPermission.MEMBER;
        category = Category.USEFUL;
        usage = "!help (command)";
        description = "Helps you find out more about commands";

    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        if(args.size() == 0) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Command help");
            embed.setFooter("ATMP6SMP");

            StringBuilder builder = new StringBuilder();
            CommandManager.getInstance().getCommands().forEach((s, command) -> {
                builder.append("**");
                builder.append(s.toUpperCase());
                builder.append("**");
                builder.append(":");
                builder.append("\n");
                builder.append(command.description);
                builder.append("\n");
                builder.append("\n");
            });

            embed.addField("", builder.toString(), true);

            event.getTextChannel().sendMessage(embed.build()).queue();
        } else if(args.size() == 1) {
            Command command = CommandManager.getInstance().getCommands().get(args.get(0).toLowerCase());

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle(args.get(0).toUpperCase() + " - Details");

            builder.addField("CATEGORY", command.category.name(), true);
            builder.addField("PERMISSION LEVEL", command.permission.name(), true);
            builder.addField("USAGE", command.usage, true);
            builder.addBlankField(false);

            StringBuilder stringBuilder = new StringBuilder();
            command.subcommands.forEach((s, sub) -> {
                stringBuilder.append("**");
                stringBuilder.append(s.toUpperCase());
                stringBuilder.append("**:");
                stringBuilder.append("\n");
                stringBuilder.append("Description: ");
                stringBuilder.append(sub.description);
                stringBuilder.append("\n");
                stringBuilder.append("Usage: ");
                stringBuilder.append(sub.usage);
                stringBuilder.append("\n");
                stringBuilder.append("\n");

            });

            if(command.subcommands.isEmpty()) {
                stringBuilder.append("No subcommands found.");
            }

            builder.addField("SUB COMMANDS", stringBuilder.toString(), false);

            event.getTextChannel().sendMessage(builder.build()).queue();
        }
    }

}
