package us.unfamousthomas.apexnerve.commands.settings;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.Text;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;
import us.unfamousthomas.apexnerve.api.objects.settings.GuildSettingsManager;

import java.util.List;

public class SettingsSystemMessagesCommand extends Command {
    public SettingsSystemMessagesCommand() {
        super("systemmessages");
        minArgs = 0;
        permission = CustomPermission.ADMIN;
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        if(args.size() == 0) {
            Long id = GuildSettingsManager.getInstance().getGuildSettings(m.getGuild().getIdLong()).getSystemTextChannel();

            String channel = event.getGuild().getTextChannelById(id).getAsMention();

            event.getTextChannel().sendMessage("Currently: " + channel).queue();
            return;
        }

        if(isNumber(args.get(0))){
            Long id = Long.parseLong(args.get(0));
            GuildSettingsManager.getInstance().getGuildSettings(m.getGuild().getIdLong()).setSystemTextChannel(id);

            String channel = event.getGuild().getTextChannelById(id).getAsMention();
            event.getTextChannel().sendMessage("Changed to: " + channel).queue();
            return;
        } if(event.getMessage().getMentionedChannels().size() == 1) {
            TextChannel channel = event.getMessage().getMentionedChannels().get(0);
            GuildSettingsManager.getInstance().getGuildSettings(m.getGuild().getIdLong()).setSystemTextChannel(channel.getIdLong());

            String mentioned = channel.getAsMention();

            event.getTextChannel().sendMessage("Changed to: " + mentioned).queue();
            return;
        } else {
            event.getTextChannel().sendMessage(Text.INVALID_USAGE.getMessage()).queue();
        }
    }

    private boolean isNumber(String num) {
        try {
            Long.parseLong(num);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
