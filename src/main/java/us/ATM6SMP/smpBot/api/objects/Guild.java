package us.ATM6SMP.smpBot.api.objects;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import us.ATM6SMP.smpBot.api.objects.user.User;

import java.util.List;

@Entity(value="guilds", noClassnameStored = true)
public class Guild {

    @Id
    ObjectId id;

    @Indexed(options = @IndexOptions(unique = true))
    Long guildId;

    @Reference(idOnly = true)
    List<User> usersInGuild;

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public void addUser(User user) {
        this.usersInGuild.add(user);
    }

    public void removeUser(User user) {
        this.usersInGuild.remove(user);
    }

    public boolean containsUser(User user) {
        return this.usersInGuild.contains(user);
    }

    public Long getGuildId() {
        return guildId;
    }

    public List<User> getUsersInGuild() {
        return usersInGuild;
    }
}
