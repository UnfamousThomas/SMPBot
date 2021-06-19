package us.unfamousthomas.apexnerve;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import us.unfamousthomas.apexnerve.api.DataHandler;
import us.unfamousthomas.apexnerve.api.Logger;
import us.unfamousthomas.apexnerve.api.commands.CommandManager;
import us.unfamousthomas.apexnerve.api.database.MongoManager;
import us.unfamousthomas.apexnerve.api.objects.settings.GuildSettingsManager;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamManager;
import us.unfamousthomas.apexnerve.api.objects.user.UserManager;
import us.unfamousthomas.apexnerve.commands.HelpCommand;
import us.unfamousthomas.apexnerve.commands.InfoCommand;
import us.unfamousthomas.apexnerve.commands.dev.ManualSaveCommand;
import us.unfamousthomas.apexnerve.commands.dev.ShutdownCommand;
import us.unfamousthomas.apexnerve.commands.leaderboards.LeaderboardCommand;
import us.unfamousthomas.apexnerve.commands.settings.SettingsCommand;
import us.unfamousthomas.apexnerve.commands.team.TeamCommand;
import us.unfamousthomas.apexnerve.commands.test.TestCommand;
import us.unfamousthomas.apexnerve.listeners.MemberLeaveJoinListener;
import us.unfamousthomas.apexnerve.listeners.TextChannelListener;
import us.unfamousthomas.apexnerve.listeners.VoiceChannelListener;

public class ApexNerveBot {
    private static ApexNerveBot instance;

    private JDA jda;

    public ApexNerveBot(String key) {
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
                   // new AnnounceCommand(),
                    new LeaderboardCommand(),
                    new ManualSaveCommand(),
                    new SettingsCommand()
            );

            jda = builder.build();

            jda.addEventListener(new VoiceChannelListener());
            jda.addEventListener(new UserManager());
            jda.addEventListener(new TextChannelListener());
            jda.addEventListener(new MemberLeaveJoinListener());

            loadManagers();

            DataHandler.getInstance().addDevs();
            Logger.log(Logger.Level.SUCCESS, "Bot started.");
        } catch (Exception e) {
            Logger.log(Logger.Level.ERROR, "Error starting. Stopping");
            System.exit(-1);
        }
    }

    private void loadManagers() {
        TeamManager.getInstance().load();
        GuildSettingsManager.getInstance().load();
    }

    public static JDA getJDA() {
        return ApexNerveBot.getInstance().jda;
    }

    public static ApexNerveBot getInstance() {
        return instance;
    }
}
