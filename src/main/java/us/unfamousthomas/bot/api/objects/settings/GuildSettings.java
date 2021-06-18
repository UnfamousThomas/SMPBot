package us.unfamousthomas.bot.api.objects.settings;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

@Entity(value="guildsettings", noClassnameStored = true)
public class GuildSettings {

    @Id
    private ObjectId id;

    @Indexed(options = @IndexOptions(unique = true))
    private Long guildId;

    private String prefix = "!";
    private int maxTeam = 3;

    public String getPrefix() {
        return prefix;
    }

    public int getMaxTeam() {
        return maxTeam;
    }

    public GuildSettings setMaxTeam(int maxTeam) {
        this.maxTeam = maxTeam;
        return this;
    }

    public GuildSettings setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getGuildId() {
        return guildId;
    }
}
