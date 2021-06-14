package us.ATM6SMP.smpBot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;

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
            pc.sendMessage("Announcement was made in channel: " + event.getTextChannel().getAsMention()).queue();
        });
    }

    private EmbedBuilder buildEmbed(String message, Member author) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Announcement");
        embedBuilder.addBlankField(false);
        embedBuilder.addField("", message, false);
        embedBuilder.setThumbnail(author.getUser().getEffectiveAvatarUrl());

        return embedBuilder;
    }
}
