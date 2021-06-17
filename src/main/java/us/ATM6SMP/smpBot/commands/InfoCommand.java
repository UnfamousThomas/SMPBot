package us.ATM6SMP.smpBot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.math3.util.Precision;
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
            event.getChannel().sendMessage(generateEmbed(m)).queue();
        } else {
            if(event.getMessage().getMentionedMembers().size() == 1) {
                Member mentioned = event.getMessage().getMentionedMembers().get(0);
                event.getGuild().retrieveMemberById(mentioned.getUser().getIdLong()).queue(member -> {
                   if(member != null) {
                       event.getChannel().sendMessage(generateEmbed(member)).queue();
                   } else {
                       event.getChannel().sendMessage("Error finding user.").queue();
                   }
                });
            } else {
                event.getChannel().sendMessage("Error finding user.").queue();
            }
        }
    }

    private MessageEmbed generateEmbed(Member member) {
        EmbedBuilder builder = new EmbedBuilder();
        User user = member.getUser();
        us.ATM6SMP.smpBot.api.objects.user.User userObject = UserManager.getInstance().getUserByMember(member, user.getName());
        builder.setTitle(user.getName() + " - PROFILE");
        builder.setColor(Color.WHITE);
        builder.setFooter("ATM6SMP BOT");
        if(userObject.checkInTeam()) {
            builder.addField("Team", userObject.getTeamName(), false);
        }
        builder.addField("Level", String.valueOf(userObject.getLevel()), true);
        builder.addField("Experience", String.valueOf(userObject.currentExperienceText()), true);
        builder.addField("Total experience over time", String.valueOf(Precision.round(userObject.getTotalExperience(), 2)), false);
        builder.setImage(user.getEffectiveAvatarUrl());

        return builder.build();
    }

}
