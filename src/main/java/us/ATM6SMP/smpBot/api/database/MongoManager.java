package us.ATM6SMP.smpBot.api.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import us.ATM6SMP.smpBot.api.database.daos.GuildSettingsDAO;
import us.ATM6SMP.smpBot.api.database.daos.InviteDAO;
import us.ATM6SMP.smpBot.api.database.daos.TeamDAO;
import us.ATM6SMP.smpBot.api.database.daos.UserDAO;
import us.ATM6SMP.smpBot.api.objects.settings.GuildSettings;
import us.ATM6SMP.smpBot.api.objects.teams.InviteObject;
import us.ATM6SMP.smpBot.api.objects.teams.TeamObject;
import us.ATM6SMP.smpBot.api.objects.user.User;

import java.util.ArrayList;
import java.util.List;

public class MongoManager {
    private MongoClient mc;
    private Morphia morphia;
    private List<MongoCredential> credentials = new ArrayList<>();

    private Datastore serverData;
    private TeamDAO teamDAO;
    private InviteDAO inviteDAO;
    private UserDAO userDAO;
    private GuildSettingsDAO guildSettingsDAO;

    public void init(String host, int port) {
        ServerAddress serverAddress = new ServerAddress(host, port);
        credentials.add(MongoCredential.createCredential("admin", "admin", "b4VXsan2GuTnss".toCharArray()));

        mc = new MongoClient(serverAddress, credentials);
        morphia = new Morphia();

        initConnections();
    }

    private void initConnections() {
        Morphia morphia = getMorphia();
        morphia.map(TeamObject.class, InviteObject.class);
        serverData = morphia.createDatastore(getClient(), "ATM6Bot");
        serverData.ensureIndexes();
        serverData.ensureCaps();

        registerDaos();
    }

    private void registerDaos() {
        teamDAO = new TeamDAO(TeamObject.class, serverData);
        inviteDAO = new InviteDAO(InviteObject.class, serverData);
        userDAO = new UserDAO(User.class, serverData);
        guildSettingsDAO = new GuildSettingsDAO(GuildSettings.class, serverData);

    }


    private Morphia getMorphia() {
        return morphia;
    }

    private MongoClient getClient() {
        return mc;
    }

    public Datastore getServerData() {
        return serverData;
    }

    //DAO GETTERS

    public TeamDAO getTeamDAO() {
        return this.teamDAO;
    }

    public InviteDAO getInviteDAO() {
        return inviteDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public GuildSettingsDAO getGuildSettingsDAO() {
        return guildSettingsDAO;
    }
}

