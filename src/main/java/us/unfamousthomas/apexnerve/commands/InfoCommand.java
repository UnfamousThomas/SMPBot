package us.unfamousthomas.apexnerve.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.math3.util.Precision;
import us.unfamousthomas.apexnerve.api.Text;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;
import us.unfamousthomas.apexnerve.api.objects.user.UserManager;

import java.awt.*;
import java.time.Year;
import java.util.Date;
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
                       event.getChannel().sendMessage(Text.USER_NOTFOUND.getMessage()).queue();
                   }
                });
            } else {
                event.getChannel().sendMessage(Text.USER_NOTFOUND.getMessage()).queue();
            }
        }
    }

    private MessageEmbed generateEmbed(Member member) {
        EmbedBuilder builder = new EmbedBuilder();
        User user = member.getUser();
        us.unfamousthomas.apexnerve.api.objects.user.User userObject = UserManager.getInstance().getUserByMember(member, user.getName());
        builder.setTitle(user.getName() + " - PROFILE");
        builder.setColor(Color.WHITE);
        builder.setFooter(Text.BOT_FOOTER.getReplaced("%year", String.valueOf(Year.now().getValue())));
        if(userObject.checkInTeam(member.getGuild())) {
            builder.addField("Team", userObject.getTeamName(member.getGuild()), false);
        }
        builder.addField("Level", String.valueOf(userObject.getLevel()), true);
        builder.addField("Experience", String.valueOf(userObject.currentExperienceText()), true);
        builder.addField("Total experience over time", String.valueOf(Precision.round(userObject.getTotalExperience(), 2)), false);
        builder.setImage(user.getEffectiveAvatarUrl());

        return builder.build();
    }

}
