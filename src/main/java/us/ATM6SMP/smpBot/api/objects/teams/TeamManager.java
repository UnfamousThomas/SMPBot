package us.ATM6SMP.smpBot.api.objects.teams;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import us.ATM6SMP.smpBot.SMPBot;
import us.ATM6SMP.smpBot.api.database.MongoManager;
import us.ATM6SMP.smpBot.api.objects.user.UserManager;

import java.awt.*;
import java.util.ArrayList;
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

    public void createTeam(Member member, Color color, String name, Guild guild) {
        TeamObject team = new TeamObject();
        team.setLeaderId(member.getIdLong());
        team.setName(name);
        team.setColor(color);
        team.addMember(member.getIdLong());

        createRole(team, guild, member);

    }

    public void createRole(TeamObject team, Guild guild, Member leader) {
        guild.createRole()
                .setColor(team.getColor())
                .setName(team.getName() + "-team")
                .setMentionable(false)
                .queue(role -> {
                    team.setRoleID(role.getIdLong());
                    guild.addRoleToMember(team.getLeaderId(), role).queue();
                    createChannels(team, guild, leader);
                });
    }

    public void createChannels(TeamObject team, Guild guild, Member leader) {
        Role role = guild.getRoleById(team.getRoleID());
        guild.createTextChannel(team.getName() + "-" + "text", guild.getCategoryById("840138217894576138"))
                .queue(textChannel -> {
                    textChannel.createPermissionOverride(role).setAllow(Permission.VIEW_CHANNEL).queue();
                    textChannel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                    team.setTeamChatChannelId(textChannel.getIdLong());
                    saveTeam(team, leader);
                });

        guild.createVoiceChannel(team.getName() + "-" + "voice", guild.getCategoryById("840138217894576138"))
                .queue(voiceChannel -> {
                    voiceChannel.createPermissionOverride(role).setAllow(Permission.VIEW_CHANNEL).queue();
                    voiceChannel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                    team.setTeamVoiceChannelId(voiceChannel.getIdLong());
                    saveTeam(team, leader);


                });

        saveTeam(team, leader);

    }

    private void saveTeam(TeamObject team, Member leader) {
        teams.remove(team);
        mongoManager.getTeamDAO().save(team);
        UserManager.getInstance().getUserByMember(leader, leader.getUser().getName()).setTeam(team);
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

        member.getGuild().getTextChannelById(team.getTeamChatChannelId()).sendMessage(member.getUser().getName() + " has been invited to the team by the team leader!").queue();
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
        joining.getGuild().addRoleToMember(joining.getUser().getIdLong(), joining.getGuild().getRoleById(joinedTeam.getRoleID())).queue();

        UserManager.getInstance().getUserByMember(joining, joining.getUser().getName()).setTeam(joinedTeam);
    }

    public void leaveTeam(Member leaving, TeamObject leftTeam) {
        leaving.getGuild().getTextChannelById(leftTeam.getTeamChatChannelId()).sendMessage(leaving.getEffectiveName() + " has left the team!").queue();

        teams.remove(leftTeam);
        leftTeam.removeMember(leaving.getUser().getIdLong());
        teams.add(leftTeam);
        mongoManager.getTeamDAO().save(leftTeam);
        leaving.getGuild().removeRoleFromMember(leaving.getUser().getIdLong(), leaving.getGuild().getRoleById(leftTeam.getRoleID())).queue();

        UserManager.getInstance().getUserByMember(leaving, leaving.getUser().getName()).setTeam(null);

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
