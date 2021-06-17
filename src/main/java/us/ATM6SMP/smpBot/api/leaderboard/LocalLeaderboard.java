package us.ATM6SMP.smpBot.api.leaderboard;

import net.dv8tion.jda.api.entities.Guild;
import org.apache.commons.math3.util.Precision;
import us.ATM6SMP.smpBot.api.objects.user.User;
import us.ATM6SMP.smpBot.api.objects.user.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalLeaderboard {

    private List<User> userList = new ArrayList<>();
    String guildName = "Not found.";

    public LocalLeaderboard(Guild guild) {
        guildName = guild.getName();

        guild.loadMembers(member -> {
            if(!(member.getUser().isBot())) {
                userList.add(UserManager.getInstance().getUserByMember(member, member.getUser().getName()));
            }
        });
    }

    public String getLeaderBoards(int amount) {
        StringBuilder builder = new StringBuilder();

        userList.sort(Collections.reverseOrder());
        int i = 1;

        if(amount > 100) {
            builder.append("Too big to calculate.");
            return builder.toString();
        }
        if(userList.size() < amount) {
            amount = userList.size();
        }

        builder.append(guildName);
        builder.append(" - Leaderboards:\n");
        while(amount >= i) {
            builder.append(i);
            builder.append(".");
            builder.append(userList.get(i - 1).getName());
            builder.append(" - ");
            builder.append(userList.get(i - 1).getLevel());
            builder.append(" (");
            builder.append(Precision.round(userList.get(i - 1).getTotalExperience(), 2));
            builder.append(" )");
            builder.append("\n");
            i = i +1;
        }

        return builder.toString();
    }
}
