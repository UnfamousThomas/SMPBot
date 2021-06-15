package us.ATM6SMP.smpBot.api.objects.user;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import us.ATM6SMP.smpBot.SMPBot;
import us.ATM6SMP.smpBot.api.database.MongoManager;
import us.ATM6SMP.smpBot.api.objects.Guild;
import us.ATM6SMP.smpBot.api.tasks.SaveUsersTaskTimer;
import us.ATM6SMP.smpBot.api.tasks.UpdateScoreboardTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class UserManager extends ListenerAdapter {
    private static HashMap<Long, User> users = new HashMap<>();

    public static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<User> userList = SMPBot.getMongoManager().getUserDAO().find().asList();
        for (User user : userList) {
            users.put(user.getDiscordId(), user);
        }
        scheduleTasks();
    }

    private void scheduleTasks() {
        Timer userTask = new Timer();
        SaveUsersTaskTimer taskTimer = new SaveUsersTaskTimer(userTask);
        userTask.scheduleAtFixedRate(taskTimer, 100, 1000 * 60 * 20);

        Timer scoreboardTask = new Timer();
        UpdateScoreboardTimer scoreboardTimer = new UpdateScoreboardTimer(scoreboardTask);
        scoreboardTask.scheduleAtFixedRate(scoreboardTimer, 100, 1000 * 5);
    }


    private User getUserByID(Long id) {
        User user;
        if(!checkIfUserExists(id)) {
            user = createNewUser(id);
        } else {
            user = users.get(id);
        }
        return user;
    }

    public boolean checkIfUserExists(Long id) {
        User user = users.get(id);

        if(user != null) {
            return true;
        } else {
            return false;
        }
    }

    public User getUserByMember(Member member, String discordName) {
        User botUser = getUserByID(member.getIdLong());
        botUser.checkName(discordName);
        guildChecks(member, botUser);

        return botUser;
    }

    private void guildChecks(Member member, User user) {
        Guild guild = SMPBot.getMongoManager().getGuildDAO().findOne("guildId", member.getGuild().getIdLong());
        if(guild == null) {
            System.out.println("Guild: " + member.getGuild().getName() + ": " + member.getGuild().getIdLong());
            guild = new Guild();
            guild.setGuildId(member.getGuild().getIdLong());
        }

        if(!(guild.containsUser(user))) {
            guild.addUser(user);
        }

        SMPBot.getMongoManager().getGuildDAO().save(guild);
    }

    public void leftGuild(Member member, User user) {
        Guild guild = SMPBot.getMongoManager().getGuildDAO().findOne("guildId", member.getGuild().getIdLong());

        if(guild == null) {
            guild = new Guild();
            guild.setGuildId(member.getIdLong());
            SMPBot.getMongoManager().getGuildDAO().save(guild);
        }

        if(guild.containsUser(user)) {
            guild.removeUser(user);
        }

        SMPBot.getMongoManager().getGuildDAO().save(guild);
    }

    public User createNewUser(Long id) {
        User user = new User();
        user.setDiscordId(id);
        return saveNewUser(user);
    }

    private User saveNewUser(User user) {
        SMPBot.getMongoManager().getUserDAO().save(user);
        users.put(user.getDiscordId(), user);

        return user;
    }

    public HashMap<Long, User> getUsers() {
        return users;
    }
}
