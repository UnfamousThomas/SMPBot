package us.unfamousthomas.apexnerve.api;

public enum Text {
    PERMISSION("You do not have enough permissions for that."),
    TEAM_NOTFOUND("The team was not found."),
    YOUR_TEAM_NOTFOUND("Your team could not be found. Please join a team first!"),
    YOUR_TEAM_LEADER_NOTFOUND("You are not the leader of your team."),
    ALREADY_IN_TEAM("You are already in a team. Please leave your current team first."),
    TEAM_CREATED("The team: %name has been created."),
    TEAM_DELETED("Team %name has been deleted."),
    INVITE_NOT_ACTIVE("That invite is not active."),
    INVITE_NOT_FOUND("Invite could not be found."),
    MAX_MEMBERCOUNT("This guild has a maximum member count of %count. The team already has %count members."),
    MENTION_EXACTLY_ONE("Please mention exactly **one** user."),
    BOT_FOOTER("Apex Nerve - %year"),
    NOT_GUILDCHANNEL("That has to be used inside a guild channel."),
    INVALID_USAGE("Invalid usage. Please use **%command** for help."),
    INVITED_TO_TEAM("**%username** has been invited to the team by the team leader."),
    JOINED_TEAM("**%username** has joined the team."),
    LEFT_TEAM("**%username** has left the team."),
    USER_PART_OF_TEAM("User is already part of a team."),
    USER_NOT_IN_YOUR_TEAM("That user is not part of your team."),
    LEADER_OF_TEAM("The leader of your team is: %name."),
    LEADER_LEAVE_FAILURE("You cannot leave your team as a leader. Please choose a new leader or delete the team."),
    COMMAND_NOTFOUND("Command was not found! Check your spelling."),
    USER_NOTFOUND("User could not be found. Try again!"),
    ANNOUNCEMENT_MADE("Announcement was successfully made in channel: %channel."),
    KICKED_TEAM("%username has been kicked from the team by the team leader."),
    KICKED_FROM("You have been kicked from the team: **%teamname**."),
    TEAM_RENAME("The team has been renamed to: **%newname**."),
    TEAM_NEW_LEADER("Team leader has been set to: **%newleader%**."),
    TEAM_COLOR_CHANGED("Team color has been changed by the team leader."),
    LEADERBOARD_TOO_BIG("The inserted number is too high to calculate the leaderboards."),
    GLOBAL_PREFIX("**Global** leaderboards:\n"),
    LOCAL_PREFIX("**%guildname** Leaderboards:\n"),
    SUCCESSFUL("Success!"),
    NUMBER_FORMAT_ERROR("Error with the number formatting. Please try again!");

    public String message;
    Text(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public String getReplaced(String target, String content) {
        return this.message.replace(target.toLowerCase(), content);
    }
}
