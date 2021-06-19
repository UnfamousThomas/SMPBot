package us.unfamousthomas.apexnerve.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import us.unfamousthomas.apexnerve.api.objects.settings.GuildSettingsManager;

public class GuildJoinLeaveListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();
        TextChannel textChannel = guild.getDefaultChannel();

        if (textChannel != null) {
            GuildSettingsManager.getInstance().getGuildSettings(guild.getIdLong()).setSystemTextChannel(textChannel.getIdLong());
        }
    }
}
