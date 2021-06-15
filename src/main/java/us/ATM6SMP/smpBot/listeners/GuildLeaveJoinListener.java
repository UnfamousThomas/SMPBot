package us.ATM6SMP.smpBot.listeners;

import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import us.ATM6SMP.smpBot.api.objects.user.User;
import us.ATM6SMP.smpBot.api.objects.user.UserManager;

public class GuildLeaveJoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
            User user = UserManager.getInstance().getUserByMember(event.getMember(), event.getMember().getUser().getName());
            UserManager.getInstance().leftGuild(event.getMember(), user);

    }


}
