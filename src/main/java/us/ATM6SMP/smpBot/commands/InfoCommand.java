package us.ATM6SMP.smpBot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.ATM6SMP.smpBot.api.commands.Command;
import us.ATM6SMP.smpBot.api.commands.CustomPermission;
import us.ATM6SMP.smpBot.api.objects.user.UserManager;

import java.awt.*;
import java.util.List;

public class InfoCommand extends Command {
    public InfoCommand() {
        super("info");
        aliases = alias("profile");
        usage = "!info (Username/mention)";
        permission = CustomPermission.MEMBER;
        minArgs = 0;
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        if(args.size() == 0) {
            event.getChannel().sendMessage(generateEmbed(m.getUser())).queue();
        } else {
            if(event.getMessage().getMentionedMembers().size() == 1) {
                Member mentioned = event.getMessage().getMentionedMembers().get(0);
                event.getChannel().sendMessage(generateEmbed(mentioned.getUser())).queue();
            } else {
                event.getChannel().sendMessage("Error finding user.").queue();
            }
        }
    }

    private MessageEmbed generateEmbed(User user) {
        EmbedBuilder builder = new EmbedBuilder();
        us.ATM6SMP.smpBot.api.objects.user.User userObject = UserManager.getInstance().getUserByID(user.getIdLong());
        builder.setTitle(user.getName() + " - PROFILE");
        builder.setColor(Color.WHITE);
        builder.setFooter("ATM6SMP BOT");
        builder.addField("Level", String.valueOf(userObject.getLevel()), true);
        builder.addField("Experience", String.valueOf(userObject.currentExperienceText()), true);
        builder.addField("Total experience over time", String.valueOf(userObject.getTotalExperience()), false);
        builder.setImage(user.getEffectiveAvatarUrl());

        return builder.build();
    }

}
