package us.ATM6SMP.smpBot.api.objects.user;

import org.apache.commons.math3.util.Precision;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;
import us.ATM6SMP.smpBot.SMPBot;
import us.ATM6SMP.smpBot.api.LevelUtils;

import java.text.DecimalFormat;

@Entity(value="users", noClassnameStored = true)
public class User {

    public User() {
    }

    @Id
    private ObjectId id;

    @Indexed(options = @IndexOptions(unique = true))
    private long discordId;

    private double totalExperience;
    private double experience;
    private int level = 0;

    public void setDiscordId(long discordId) {
        this.discordId = discordId;
    }

    public long getDiscordId() {
        return discordId;
    }

    private void levelUp() {
        experience = experience - getRequiredExperienceForNextLevel();
        level = level + 1;
        SMPBot.getJDA().retrieveUserById(this.discordId).queue(user -> {
            user.openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage("You have levelled up to: " + level + ". " + percentageText()).queue();
            });
        });
    }

    public String percentageText() {
        return "You are " + roundedDouble(this.getPercentage()) + "% to level " + (this.level + 1) + "!";
    }

    public String currentExperienceText() {
        return roundedDouble(this.experience) + " / " + roundedDouble(this.getRequiredExperienceForNextLevel()) + " (" + roundedDouble(this.getPercentage()) + "%)";
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
        this.totalExperience = this.totalExperience + totalExperience;
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

    public double getTotalExperience() {
        return totalExperience;
    }

    public void recalculateTotalExperience() {
        int level = this.level;

        this.totalExperience = (100 * Math.pow(level, 2)) + this.experience;
    }

    private double roundedDouble(double input) {
        return Precision.round(input, 2);
    }
}
