package us.ATM6SMP.smpBot.teams;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import us.ATM6SMP.smpBot.SMPBot;
import us.ATM6SMP.smpBot.api.database.MongoManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TeamManager {
    public static TeamManager instance;
    MongoManager mongoManager = SMPBot.getMongoManager();
    ArrayList<TeamObject> teams = new ArrayList<>();
    ArrayList<InviteObject> invites = new ArrayList<>();

    public static TeamManager getInstance() {
        if (instance == null) {
            instance = new TeamManager();
        }
        return instance;
    }

    public void load() {
        List<TeamObject> teamsLoaded = SMPBot.getMongoManager().getTeamDAO().find().asList();
        teams.addAll(teamsLoaded);

        List<InviteObject> invitesLoaded = SMPBot.getMongoManager().getInviteDAO().find().asList();
        invites.addAll(invitesLoaded);
    }

    public void createTeam(long leader, Color color, String name, Guild guild) {
        TeamObject team = new TeamObject();
        team.setLeaderId(leader);
        team.setName(name);
        team.setColor(color);
        team.addMember(leader);

        createRole(team, guild);
    }

    public void createRole(TeamObject team, Guild guild) {
        guild.createRole()
                .setColor(team.getColor())
                .setName(team.getName() + "-team")
                .setMentionable(false)
                .queue(role -> {
                    team.setRoleID(role.getIdLong());
                    guild.addRoleToMember(team.getLeaderId(), role).queue();
                    createChannels(team, guild);
                });
    }

    public void createChannels(TeamObject team, Guild guild) {
        Role role = guild.getRoleById(team.getRoleID());
        guild.createTextChannel(team.getName() + "-" + "text", guild.getCategoryById("840138217894576138"))
                .queue(textChannel -> {
                    textChannel.createPermissionOverride(role).setAllow(Permission.VIEW_CHANNEL).queue();
                    textChannel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                    team.setTeamChatChannelId(textChannel.getIdLong());
                    saveTeam(team);
                });

        guild.createVoiceChannel(team.getName() + "-" + "voice", guild.getCategoryById("840138217894576138"))
                .queue(voiceChannel -> {
                    voiceChannel.createPermissionOverride(role).setAllow(Permission.VIEW_CHANNEL).queue();
                    voiceChannel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                    team.setTeamVoiceChannelId(voiceChannel.getIdLong());
                    saveTeam(team);


                });

        saveTeam(team);

    }

    private void saveTeam(TeamObject team) {
        teams.remove(team);
        mongoManager.getTeamDAO().save(team);
        teams.add(team);
    }

    public void deleteTeam(TeamObject team, Guild guild) {
        teams.remove(team);

        TextChannel textChannel = guild.getTextChannelById(team.getTeamChatChannelId());
        VoiceChannel voiceChannel = guild.getVoiceChannelById(team.getTeamVoiceChannelId());
        Role role = guild.getRoleById(team.getRoleID());

        if (textChannel != null) {
            textChannel.delete().queueAfter(1, TimeUnit.SECONDS);
        }
        if (voiceChannel != null) {
            voiceChannel.delete().queueAfter(2, TimeUnit.SECONDS);
        }
        if (role != null) {
            role.delete().queue();
        }
        team.getInvites().forEach(inviteObject -> {
            mongoManager.getInviteDAO().delete(inviteObject);
        });

        mongoManager.getTeamDAO().delete(team);
    }

    public void inviteToTeam(TeamObject team, Member member) {

        InviteObject newInvite = new InviteObject();
        newInvite.setActive(true);
        newInvite.setMemberSentTo(member.getIdLong());
        newInvite.setTimeInvitedAt(System.currentTimeMillis());
        newInvite.setMemberSentFrom(team.getLeaderId());

        mongoManager.getInviteDAO().save(newInvite);
        invites.add(newInvite);

        teams.remove(team);
        team.addInvite(newInvite);
        teams.add(team);
        mongoManager.getTeamDAO().save(team);

        sendMessage(member, team, member.getGuild());
    }

    private void sendMessage(Member member, TeamObject team, Guild guild) {
        member.getUser().openPrivateChannel().queue(privateChannel -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder
                    .setColor(team.getColor())
                    .setTitle(team.getName() + " - Invite!")
                    .setFooter("ATM6 SMP")
                    .addField("Team name", team.getName(), true);

            embedBuilder
                    .addBlankField(false)
                    .addField("To join: ", "!team join " + team.getLeaderId(), false)
                    .addField("To deny:", "!team deny " + team.getLeaderId(), false);
            MessageEmbed embed = embedBuilder.build();

            privateChannel.sendMessage(embed).queue();
        });
    }

    public void joinTeam(Member joining, TeamObject joinedTeam, InviteObject inviteObject) {
        joining.getGuild().getTextChannelById(joinedTeam.getTeamChatChannelId()).sendMessage(joining.getEffectiveName() + " has joined the team!").queue();

        invites.remove(inviteObject);
        inviteObject.setActive(false);
        invites.add(inviteObject);
        mongoManager.getInviteDAO().save(inviteObject);

        teams.remove(joinedTeam);
        joinedTeam.addMember(joining.getUser().getIdLong());
        teams.add(joinedTeam);
        mongoManager.getTeamDAO().save(joinedTeam);
    }

    public void denyInvite(Member invited, TeamObject team, InviteObject invite) {
        invited.getGuild().getTextChannelById(team.getTeamChatChannelId()).sendMessage(invited.getEffectiveName() + " has denied the invite to the team!").queue();

        invites.remove(invite);
        invite.setActive(false);
        invites.add(invite);
        mongoManager.getInviteDAO().save(invite);
    }

    public ArrayList<TeamObject> getTeams() {
        return teams;
    }

    public ArrayList<InviteObject> getInvites() {
        return invites;
    }
}
