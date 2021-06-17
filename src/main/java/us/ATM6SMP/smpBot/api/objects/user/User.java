package us.ATM6SMP.smpBot.api.objects.user;

import org.apache.commons.math3.util.Precision;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.annotations.*;
import us.ATM6SMP.smpBot.SMPBot;
import us.ATM6SMP.smpBot.api.LevelUtils;
import us.ATM6SMP.smpBot.api.objects.teams.TeamObject;

import java.util.ArrayList;

@Entity(value="users", noClassnameStored = true)
public class User implements Comparable<User> {

    public User() { }

    @Id
    private ObjectId id;

    @Indexed(options = @IndexOptions(unique = true))
    private long discordId;

    private String currentDiscordName;
    private ArrayList<String> previousDiscordNames;
    private double totalExperience;
    private double experience;
    private int level = 0;

    @Reference
    private TeamObject team;


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

        checkForLevelup();
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

        if(this.experience >= required) {
            levelUp();
        }
    }

    public void giveExperience(double expAdded) {
        this.experience = this.experience + expAdded;
        this.totalExperience = this.totalExperience + expAdded;
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

    public Double getTotalExperience() {
        return totalExperience;
    }

    public void recalculateTotalExperience() {
        int level = this.level;

        this.totalExperience = (100 * Math.pow(level, 2)) + this.experience;
    }

    private double roundedDouble(double input) {
        return Precision.round(input, 2);
    }

    private int compare (double x, double y) {
        return Double.compare(x, y);

    }

    public void checkName(String currentName) {
        if(!currentName.equals(this.currentDiscordName)) {
            setCurrentDiscordName(currentName);
        }
    }
    private void setCurrentDiscordName(String currentDiscordName) {
        if(this.currentDiscordName != null) {
            addPreviousDiscordName(this.currentDiscordName);
        }
        this.currentDiscordName = currentDiscordName;
    }
    private void addPreviousDiscordName(String previousname) {
        this.previousDiscordNames.add(previousname);
    }

    public String getName() {
        return currentDiscordName;
    }

    @Override
    public int compareTo(@NotNull User o) {
        return this.getTotalExperience().compareTo(((User) o).getTotalExperience());
    }

    public void setTeam(TeamObject team) {
        this.team = team;
    }

    public TeamObject getTeam() {
        return team;
    }

    public String getTeamName() {
        if(getTeam() != null) {
            return getTeam().getName();
        } else {
            return "Not in a team";
        }
    }

    public boolean checkInTeam() {
        if(getTeam() != null) {
            return true;
        } else {
            return false;
        }
    }


}
