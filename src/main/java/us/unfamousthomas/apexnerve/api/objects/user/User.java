package us.unfamousthomas.apexnerve.api.objects.user;

import net.dv8tion.jda.api.entities.Guild;
import org.apache.commons.math3.util.Precision;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.annotations.*;
import us.unfamousthomas.apexnerve.ApexNerveBot;
import us.unfamousthomas.apexnerve.api.Utils;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamObject;

import java.util.ArrayList;
import java.util.HashMap;

@Entity(value="users", noClassnameStored = true)
public class User implements Comparable<User> {

    public User() { }

    @Id
    private ObjectId id;

    @Indexed(options = @IndexOptions(unique = true))
    private long discordId;

    private String currentDiscordName;
    private ArrayList<String> previousDiscordNames = new ArrayList<>();
    private double totalExperience;
    private double experience;
    private int level = 0;

    @Reference
    private HashMap<Long, TeamObject> teamPerGuild = new HashMap<>();


    public void setDiscordId(long discordId) {
        this.discordId = discordId;
    }

    public long getDiscordId() {
        return discordId;
    }

    private void levelUp() {
        experience = experience - getRequiredExperienceForNextLevel();
        level = level + 1;
        ApexNerveBot.getJDA().retrieveUserById(this.discordId).queue(user -> {
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
        double required = Utils.experienceFormula(level + 1);

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
        return Utils.experienceFormula(level + 1);
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
        if(team == null) {
            throw new NullPointerException();
        }
        this.teamPerGuild.put(team.getGuildID(), team);
    }

    public void leaveTeam(Guild guild) {
        this.teamPerGuild.remove(guild.getIdLong());
    }

    public TeamObject getTeam(Guild guild) {
        return teamPerGuild.getOrDefault(guild.getIdLong(), null);
    }

    public String getTeamName(Guild guild) {
        if(getTeam(guild) != null) {
            return getTeam(guild).getName();
        } else {
            return "Not in a team";
        }
    }

    public boolean checkInTeam(Guild guild) {
        if(getTeam(guild) != null) {
            return true;
        } else {
            return false;
        }
    }


}
