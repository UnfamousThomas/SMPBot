package us.unfamousthomas.apexnerve.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.Text;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnnounceCommand extends Command {
    public AnnounceCommand() {
        super("announce");
        aliases = alias("announcement", "news");
        permission = CustomPermission.ADMIN;
        usage = "!announce (message)";
        maxArgs = 1;
    }

    //todo redo announce cmd to enable sending longer messages
    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        for (EmbedBuilder embedBuilder : buildEmbeds(String.join(" ", args), m)) {
            event.getTextChannel().sendMessage(embedBuilder.build()).queue();
        }
        m.getUser().openPrivateChannel().queue(pc -> {
            pc.sendMessage(Text.ANNOUNCEMENT_MADE.getReplaced("%channel", event.getTextChannel().getAsMention())).queue();
        });
    }

    private List<EmbedBuilder> buildEmbeds(String message, Member author) {
        List<EmbedBuilder> builders = new ArrayList<>();
        String[] lines = message.split("\\r?\\n");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Announcement");
        embedBuilder.setThumbnail(author.getUser().getEffectiveAvatarUrl());

        builders.add(embedBuilder);

        for (String s : Arrays.asList(lines)) {
            EmbedBuilder embedLoopBuilder = new EmbedBuilder();
            embedBuilder.addField("", s, false);

            builders.add(embedLoopBuilder);
        }

        return builders;
    }
}
