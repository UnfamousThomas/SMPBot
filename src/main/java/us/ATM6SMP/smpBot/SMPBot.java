package us.ATM6SMP.smpBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import us.ATM6SMP.smpBot.api.commands.CommandManager;
import us.ATM6SMP.smpBot.api.Logger;
import us.ATM6SMP.smpBot.api.database.MongoManager;
import us.ATM6SMP.smpBot.commands.HelpCommand;
import us.ATM6SMP.smpBot.commands.team.TeamCommand;
import us.ATM6SMP.smpBot.commands.test.TestCommand;
import us.ATM6SMP.smpBot.teams.TeamManager;

public class SMPBot {

    public static JDA jda;
    private static MongoManager mongoManager;

    public static void main(String[] args) {
        mongoManager = new MongoManager();
        mongoManager.init("54.39.243.170", 27017);
        Logger.log(Logger.Level.INFO, "Mongo initiated and connected.");
        try {
            JDABuilder builder = JDABuilder.createDefault("ODQzMjEzNDIyMzE3NjY2MzI1.YKAlsg.Nrdol1HLNTMfBaum35xqlW12hyk");
            builder.setActivity(Activity.playing("on the SMP!"));
            builder.setBulkDeleteSplittingEnabled(false);

            TeamManager.getInstance().load();

            CommandManager.init(builder);
            CommandManager.registerCommands(
                    new TestCommand(),
                    new TeamCommand(),
                    new HelpCommand()
            );
            jda = builder.build();
            Logger.log(Logger.Level.SUCCESS, "Bot started.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static MongoManager getMongoManager() {
        return mongoManager;
    }

    public static JDA getJDA() {
        return jda;
    }
}
