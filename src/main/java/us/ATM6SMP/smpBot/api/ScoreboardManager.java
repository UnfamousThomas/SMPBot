package us.ATM6SMP.smpBot.api;

import net.dv8tion.jda.api.entities.Guild;
import org.mongodb.morphia.query.Query;
import us.ATM6SMP.smpBot.SMPBot;
import us.ATM6SMP.smpBot.api.database.daos.GuildDAO;
import us.ATM6SMP.smpBot.api.objects.user.User;
import us.ATM6SMP.smpBot.api.objects.user.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ScoreboardManager {
    public static ScoreboardManager instance;
    private static HashMap<Long, String> scoreboardMap = new HashMap<>();

    public static ScoreboardManager getInstance() {
        if (instance == null) {
            instance = new ScoreboardManager();
        }
        return instance;
    }

    public String requestScoreboard(Guild guild) {
        return scoreboardMap.get(guild.getIdLong());
    }

    public void updateScoreboard() {
        //triggers every 30 secs I suppose
        List<us.ATM6SMP.smpBot.api.objects.Guild> guilds = SMPBot.getMongoManager().getGuildDAO().find().asList();
        for (us.ATM6SMP.smpBot.api.objects.Guild guild : guilds) {
            List<User> users= guild.getUsersInGuild();
            Collections.sort(users);
            int rank = 1;
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("LEADERBOARD:\n");
            for (User user : users) {
                if(rank > 11) {
                    break;
                }
                stringBuilder.append("**");
                stringBuilder.append(rank);
                stringBuilder.append("**");
                stringBuilder.append(user.getName());
            }
            String built = stringBuilder.toString();
            scoreboardMap.put(guild.getGuildId(), built);

        }
    }
}
