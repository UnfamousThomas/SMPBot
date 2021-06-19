package us.unfamousthomas.apexnerve.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.Text;
import us.unfamousthomas.apexnerve.api.commands.Category;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CommandManager;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;

import java.time.Year;
import java.util.List;

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
            embed.setFooter(Text.BOT_FOOTER.getReplaced("%year", String.valueOf(Year.now().getValue())));

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
            if(command == null) {
                event.getChannel().sendMessage(Text.COMMAND_NOTFOUND.getMessage()).queue();
                return;
            }
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
