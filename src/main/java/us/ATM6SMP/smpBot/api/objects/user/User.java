package us.ATM6SMP.smpBot.api.objects.user;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;
import us.ATM6SMP.smpBot.SMPBot;
import us.ATM6SMP.smpBot.api.LevelUtils;

@Entity(value="users", noClassnameStored = true)
public class User {

    public User() {
    }

    @Id
    private ObjectId id;

    @Indexed(options = @IndexOptions(unique = true))
    private long discordId;

    private double experience;
    private int level = 0;

    public void setDiscordId(long discordId) {
        this.discordId = discordId;
    }

    public long getDiscordId() {
        return discordId;
    }

    private void levelUp() {
        level = level + 1;
        SMPBot.getJDA().retrieveUserById(this.discordId).queue(user -> {
            user.openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage("You have levelled up to: " + level + ". You are " + getPercentage() + "% to level " + (level + 1) + "!").queue();
            });
        });
    }

    public double getPercentage() {
        return ((experience / getRequiredExperienceForNextLevel()) * 100);
    }

    public int getLevel() {
        return level;
    }

    public double getExperience() {
        return experience;
    }

    private void checkForLevelup() {
        double required = LevelUtils.experienceFormula(level + 1);

        if(experience >= required) {
            levelUp();
        }
    }

    public void giveExperience(double expAdded) {
        this.experience = this.experience + expAdded;
        checkForLevelup();
    }

    public double getRequiredExperienceForNextLevel() {
        return LevelUtils.experienceFormula(level + 1);
    }

    private double getExperienceMissing() {
        double missing = getRequiredExperienceForNextLevel() - experience;
        if (missing < 0) {
            missing = 0;
        }
        return missing;
    }

}
