package us.unfamousthomas.apexnerve.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.Text;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;

import java.util.List;

public class AnnounceCommand extends Command {
    public AnnounceCommand() {
        super("announce");
        aliases = alias("announcement", "news");
        permission = CustomPermission.ADMIN;
        usage = "!announce (message)";
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getTextChannel().sendMessage(buildEmbed(String.join(" ", args), m).build()).queue();
        m.getUser().openPrivateChannel().queue(pc -> {
            pc.sendMessage(Text.ANNOUNCEMENT_MADE.getReplaced("%channel", event.getTextChannel().getAsMention())).queue();
        });
    }

    private EmbedBuilder buildEmbed(String message, Member author) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Announcement");
        embedBuilder.addField("", message, false);
        embedBuilder.setThumbnail(author.getUser().getEffectiveAvatarUrl());

        return embedBuilder;
    }
}
