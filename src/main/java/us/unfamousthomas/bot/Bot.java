package us.unfamousthomas.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import us.unfamousthomas.bot.api.DataHandler;
import us.unfamousthomas.bot.api.Logger;
import us.unfamousthomas.bot.api.commands.CommandManager;
import us.unfamousthomas.bot.api.database.MongoManager;
import us.unfamousthomas.bot.api.objects.settings.GuildSettingsManager;
import us.unfamousthomas.bot.api.objects.teams.TeamManager;
import us.unfamousthomas.bot.api.objects.user.UserManager;
import us.unfamousthomas.bot.commands.AnnounceCommand;
import us.unfamousthomas.bot.commands.HelpCommand;
import us.unfamousthomas.bot.commands.InfoCommand;
import us.unfamousthomas.bot.commands.dev.ManualSaveCommand;
import us.unfamousthomas.bot.commands.dev.ShutdownCommand;
import us.unfamousthomas.bot.commands.leaderboards.LeaderboardCommand;
import us.unfamousthomas.bot.commands.team.TeamCommand;
import us.unfamousthomas.bot.commands.test.TestCommand;
import us.unfamousthomas.bot.listeners.GuildLeaveJoinListener;
import us.unfamousthomas.bot.listeners.TextChannelListener;
import us.unfamousthomas.bot.listeners.VoiceChannelListener;

public class Bot {
    private static Bot instance;

    private JDA jda;

    public Bot(String key) {
        instance = this;

        new MongoManager("54.39.243.170", 27017);
        Logger.log(Logger.Level.INFO, "Mongo initiated and connected.");
        try {
            JDABuilder builder = JDABuilder
                    .createDefault(key)
                    .setActivity(Activity.playing("on the SMP!"))
                    .setBulkDeleteSplittingEnabled(false)
                    .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                    .enableIntents(GatewayIntent.GUILD_MEMBERS);

            CommandManager.init(builder);
            CommandManager.registerCommands(
                    new TestCommand(),
                    new TeamCommand(),
                    new HelpCommand(),
                    new ShutdownCommand(),
                    new InfoCommand(),
                    new AnnounceCommand(),
                    new LeaderboardCommand(),
                    new ManualSaveCommand()
            );

            jda = builder.build();

            jda.addEventListener(new VoiceChannelListener());
            jda.addEventListener(new UserManager());
            jda.addEventListener(new TextChannelListener());
            jda.addEventListener(new GuildLeaveJoinListener());

            loadManagers();

            DataHandler.getInstance().addDevs();
            Logger.log(Logger.Level.SUCCESS, "Bot started.");
        } catch (Exception e) {
            Logger.log(Logger.Level.ERROR, "Error starting. Stopping");
            System.exit(-1);        }
    }

    private void loadManagers() {
        TeamManager.getInstance().load();
        GuildSettingsManager.getInstance().load();
    }

    public static JDA getJDA() {
        return Bot.getInstance().jda;
    }

    public static Bot getInstance() {
        return instance;
    }
}
