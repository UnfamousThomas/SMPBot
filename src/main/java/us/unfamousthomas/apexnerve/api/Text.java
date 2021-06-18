package us.unfamousthomas.apexnerve.api;

public enum Text {
    PERMISSION("You do not have enough permissions for that."),
    TEAM_NOTFOUND("The team was not found."),
    YOUR_TEAM_NOTFOUND("Your team could not be found. Please join a team first!"),
    YOUR_TEAM_LEADER_NOTFOUND("You are not the leader of your team."),
    BOT_FOOTER("Apex Nerve - %year"),
    NOT_GUILDCHANNEL("That has to be used inside a guild channel."),
    INVALID_USAGE("Invalid usage. Please use **%command** for help."),
    INVITED_TO_TEAM("**%username** has been invited to the team by the team leader."),
    JOINED_TEAM("**%username** has joined the team."),
    LEFT_TEAM("**%username** has left the team."),
    KICKED_TEAM("%username has been kicked from the team by the team leader."),
    KICKED_FROM("You have been kicked from the team: **%teamname**."),
    TEAM_RENAME("The team has been renamed to: **%newname**."),
    TEAM_NEW_LEADER("Team leader has been set to: **%newleader%**."),
    TEAM_COLOR_CHANGED("Team color has been changed by the team leader.");
    public String message;
    Text(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public String getReplaced(String target, String content) {
        return this.message.replace(target, content);
    }
}
