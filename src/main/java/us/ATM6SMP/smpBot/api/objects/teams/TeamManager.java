package us.ATM6SMP.smpBot.api.objects.teams;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import us.ATM6SMP.smpBot.SMPBot;
import us.ATM6SMP.smpBot.api.database.MongoManager;
import us.ATM6SMP.smpBot.api.objects.user.User;
import us.ATM6SMP.smpBot.api.objects.user.UserManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TeamManager {
    public static TeamManager instance;
    MongoManager mongoManager = SMPBot.getMongoManager();
    //ArrayList<TeamObject> teams = new ArrayList<>();
    //ArrayList<InviteObject> invites = new ArrayList<>();
    Map<Long, ArrayList<TeamObject>> teamsMap = new HashMap<>();
    Map<Long, ArrayList<InviteObject>> invitesMap = new HashMap<>();
    public static TeamManager getInstance() {
        if (instance == null) {
            instance = new TeamManager();
        }
        return instance;
    }

    public void load() {
        List<TeamObject> teamsLoaded = SMPBot.getMongoManager().getTeamDAO().find().asList();
        for (TeamObject teamObject : teamsLoaded) {
            if(teamsMap.containsKey(teamObject.getGuildID())) {
                ArrayList<TeamObject> teamListForGuild = teamsMap.get(teamObject.getGuildID());
                teamListForGuild.add(teamObject);

                teamsMap.put(teamObject.getGuildID(), teamListForGuild);
            } else {
                ArrayList<TeamObject> teamListForGuild = new ArrayList<>();
                teamListForGuild.add(teamObject);

                teamsMap.put(teamObject.getGuildID(), teamListForGuild);
            }
        }
        List<InviteObject> invitesLoaded = SMPBot.getMongoManager().getInviteDAO().find().asList();

        for (InviteObject inviteObject : invitesLoaded) {
            if(invitesMap.containsKey(inviteObject.getGuildId())) {
                ArrayList<InviteObject> inviteListForGuild = invitesMap.get(inviteObject.getGuildId());
                inviteListForGuild.add(inviteObject);

                invitesMap.put(inviteObject.getGuildId(), inviteListForGuild);
            } else {
                ArrayList<InviteObject> inviteListForGuild = new ArrayList<>();
                inviteListForGuild.add(inviteObject);

                invitesMap.put(inviteObject.getGuildId(), inviteListForGuild);
            }
        }
    }

    public void createTeam(Member member, Color color, String name, Guild guild) {
        TeamObject team = new TeamObject();
        team.setLeaderId(member.getIdLong());
        team.setName(name);
        team.setColor(color);
        team.addMember(member.getIdLong());
        team.setGuildID(member.getGuild().getIdLong());

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
                });

        guild.createVoiceChannel(team.getName() + "-" + "voice", guild.getCategoryById("840138217894576138"))
                .queue(voiceChannel -> {
                    voiceChannel.createPermissionOverride(role).setAllow(Permission.VIEW_CHANNEL).queue();
                    voiceChannel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                    team.setTeamVoiceChannelId(voiceChannel.getIdLong());


                });

        saveTeam(team, leader);

        User user = UserManager.getInstance().getUserByMember(leader ,leader.getUser().getName());
        user.setTeam(team);
        SMPBot.getMongoManager().getUserDAO().save(user);
    }

    private void saveTeam(TeamObject team, Member leader) {
        checkEmptyTeam(leader.getGuild());

        if(teamsMap.get(leader.getGuild().getIdLong()).contains(team)) {
            removeTeam(team, leader.getGuild());
        }
        mongoManager.getTeamDAO().save(team);
        addTeam(team, leader.getGuild());

    }

    private void checkEmptyTeam(Guild guild) {
        if(!teamsMap.containsKey(guild.getIdLong())) {
            ArrayList<TeamObject> list = new ArrayList<>();

            teamsMap.put(guild.getIdLong(), list);
        }
    }

    private void checkEmptyInvite(Guild guild) {
        if(!invitesMap.containsKey(guild.getIdLong())) {
            ArrayList<InviteObject> list = new ArrayList<>();

            invitesMap.put(guild.getIdLong(), list);
        }
    }


    public void deleteTeam(TeamObject team, Guild guild) {
        removeTeam(team, guild);
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
        addInvite(newInvite, member.getGuild());

        removeTeam(team, member.getGuild());
        team.addInvite(newInvite);
        addTeam(team, member.getGuild());
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

        removeInvite(inviteObject, joining.getGuild());
        inviteObject.setActive(false);
        addInvite(inviteObject, joining.getGuild());
        mongoManager.getInviteDAO().save(inviteObject);

        removeTeam(joinedTeam, joining.getGuild());
        joinedTeam.addMember(joining.getUser().getIdLong());
        addTeam(joinedTeam, joining.getGuild());
        mongoManager.getTeamDAO().save(joinedTeam);
        joining.getGuild().addRoleToMember(joining.getUser().getIdLong(), joining.getGuild().getRoleById(joinedTeam.getRoleID())).queue();

        UserManager.getInstance().getUserByMember(joining, joining.getUser().getName()).setTeam(joinedTeam);
    }

    public void leaveTeam(Member leaving, TeamObject leftTeam) {
        leaving.getGuild().getTextChannelById(leftTeam.getTeamChatChannelId()).sendMessage(leaving.getEffectiveName() + " has left the team!").queue();

        removeTeam(leftTeam, leaving.getGuild());
        leftTeam.removeMember(leaving.getUser().getIdLong());
        addTeam(leftTeam, leaving.getGuild());
        mongoManager.getTeamDAO().save(leftTeam);
        leaving.getGuild().removeRoleFromMember(leaving.getUser().getIdLong(), leaving.getGuild().getRoleById(leftTeam.getRoleID())).queue();

        UserManager.getInstance().getUserByMember(leaving, leaving.getUser().getName()).leaveTeam(leaving.getGuild());

    }

    public void kickTeam(Member leaving, TeamObject leftTeam) {
        leaving.getGuild().getTextChannelById(leftTeam.getTeamChatChannelId()).sendMessage(leaving.getEffectiveName() + " has been kicked from the team!").queue();

        removeTeam(leftTeam, leaving.getGuild());
        leftTeam.removeMember(leaving.getUser().getIdLong());
        addTeam(leftTeam, leaving.getGuild());
        mongoManager.getTeamDAO().save(leftTeam);
        leaving.getGuild().removeRoleFromMember(leaving.getUser().getIdLong(), leaving.getGuild().getRoleById(leftTeam.getRoleID())).queue();

        UserManager.getInstance().getUserByMember(leaving, leaving.getUser().getName()).leaveTeam(leaving.getGuild());

        leaving.getUser().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("You have been kicked from the team " + leftTeam.getName()).queue();
        });

    }

    public void renameTeam(String newName, TeamObject team, Member leader) {
        Guild guild = leader.getGuild();
        removeTeam(team, leader.getGuild());

        //set new team name
        team.setName(newName);
        //set new role name
        guild.getRoleById(team.getRoleID()).getManager().setName(newName + "-team").queue();

        guild.getTextChannelById(team.getTeamChatChannelId()).getManager().setName(newName +"-text").queue();

        guild.getVoiceChannelById(team.getTeamVoiceChannelId()).getManager().setName(newName +"-voice").queue();

        mongoManager.getTeamDAO().save(team);
        addTeam(team, leader.getGuild());
        guild.getTextChannelById(team.getTeamChatChannelId()).sendMessage("Team has been renamed to: " + newName).queue();

    }

    public void setNewLeader(Member currentLeader, Member newLeader, TeamObject team) {
        removeTeam(team, currentLeader.getGuild());
        team.setLeaderId(newLeader.getIdLong());
        for (InviteObject invite : team.getInvites()) {
            invite.setActive(false);
            SMPBot.getMongoManager().getInviteDAO().save(invite);
        }
        addTeam(team, newLeader.getGuild());
        newLeader.getGuild().getTextChannelById(team.getTeamChatChannelId()).sendMessage("New team leader has been assigned: " + newLeader.getUser().getName()).queue();

        SMPBot.getMongoManager().getTeamDAO().save(team);
    }

    public void recolorTeam(Color color, TeamObject teamObject, Member leader) {
        Guild guild = leader.getGuild();
        removeTeam(teamObject, leader.getGuild());

        teamObject.setColor(color);

        guild.getRoleById(teamObject.getRoleID()).getManager().setColor(color).queue();

        mongoManager.getTeamDAO().save(teamObject);
        removeTeam(teamObject, leader.getGuild());

        guild.getTextChannelById(teamObject.getTeamChatChannelId()).sendMessage("Team color has been changed.").queue();
    }

    public void denyInvite(Member invited, TeamObject team, InviteObject invite) {
        invited.getGuild().getTextChannelById(team.getTeamChatChannelId()).sendMessage(invited.getEffectiveName() + " has denied the invite to the team!").queue();

        removeInvite(invite, invited.getGuild());
        invite.setActive(false);
        addInvite(invite, invited.getGuild());
        mongoManager.getInviteDAO().save(invite);
    }

    public ArrayList<TeamObject> getTeams(Guild guild) {
        return teamsMap.get(guild);
    }

    public ArrayList<InviteObject> getInvites(Guild guild) {
        return invitesMap.get(guild);
    }

    private void removeTeam(TeamObject team, Guild guild) {
        checkEmptyTeam(guild);
        ArrayList<TeamObject> teams = teamsMap.get(guild.getIdLong());

        teams.remove(team);

        teamsMap.put(guild.getIdLong(), teams);
    }

    private void addTeam(TeamObject team, Guild guild) {
        checkEmptyTeam(guild);
        ArrayList<TeamObject> teams = teamsMap.get(guild.getIdLong());

        teams.add(team);

        teamsMap.put(guild.getIdLong(), teams);
    }

    private void removeInvite(InviteObject invite, Guild guild) {
        checkEmptyInvite(guild);
        ArrayList<InviteObject> invites = invitesMap.get(guild.getIdLong());

        invites.remove(invite);

        invitesMap.put(guild.getIdLong(), invites);
    }

    private void addInvite(InviteObject invite, Guild guild) {
        checkEmptyInvite(guild);
        ArrayList<InviteObject> invites = invitesMap.get(guild.getIdLong());

        invites.add(invite);

        invitesMap.put(guild.getIdLong(), invites);
    }

    public TeamObject getTeamMemberOf(Member searching) {
        return UserManager.getInstance().getUserByMember(searching, searching.getUser().getName()).getTeam(searching.getGuild());
    }
    public TeamObject getTeamMemberOfById(Long searchingId, Guild guild) {
        return UserManager.getInstance().getUserById(searchingId).getTeam(guild);
    }


    public ArrayList<InviteObject> getInviteFromGuild(long guildId) {
        return invitesMap.get(guildId);
    }

    public InviteObject getInviteFromGuildByLeaderId(long leaderId, long userId, Guild guild) {
        checkEmptyInvite(guild);
        InviteObject invite = null;
        if(!invitesMap.containsKey(guild.getIdLong())) {
            return invite;
        }
        for (InviteObject inviteObject : getInviteFromGuild(guild.getIdLong())) {
            if(inviteObject.getMemberSentFrom() == leaderId && userId == inviteObject.getMemberSentTo()) {
                invite = inviteObject;
            }
        }
        return invite;
    }

    public TeamObject getTeamLeaderOf(Member searching) {
        TeamObject team = getTeamMemberOf(searching);

        if(team != null) {
            if(team.getLeaderId() == searching.getIdLong()) {
                return team;
            }
        }

        return null;
    }

    public TeamObject getTeamLeaderOfById(long id, Guild guild) {
        TeamObject team = getTeamMemberOfById(id, guild);

        if(team != null) {
            if(team.getLeaderId() == id) {
                return team;
            }
        }

        return null;
    }
}
