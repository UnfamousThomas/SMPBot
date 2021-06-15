package us.ATM6SMP.smpBot.listeners;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import us.ATM6SMP.smpBot.api.LevelUtils;
import us.ATM6SMP.smpBot.api.objects.user.UserManager;

public class TextChannelListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) {
            return;
        }

        if(event.getChannel().getType() == ChannelType.PRIVATE) return;
        if(event.getChannel().getType() == ChannelType.UNKNOWN) return;
        if(event.getChannel().getType() == ChannelType.STORE) return;
        if(event.getChannel().getType() == ChannelType.GROUP) return;

        UserManager.getInstance().getUserByMember(event.getMember(), event.getAuthor().getName()).giveExperience(LevelUtils.getExperienceFromMessages(1));
    }
}
