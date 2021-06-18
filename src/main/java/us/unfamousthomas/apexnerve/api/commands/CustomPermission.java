package us.unfamousthomas.apexnerve.api.commands;

public enum CustomPermission {
    ADMIN("ADMINISTRATOR"),
    MODERATOR("KICK_MEMBERS"),
    MEMBER("MESSAGE_READ"),
    BOTHELPER("BOTHELPER"),
    DEV("DEV");

    public String perm;

    CustomPermission(String perm) {
        this.perm = perm;
    }
}
