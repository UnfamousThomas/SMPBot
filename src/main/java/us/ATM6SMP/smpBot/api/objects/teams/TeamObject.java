package us.ATM6SMP.smpBot.api.objects.teams;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.awt.*;
import java.util.ArrayList;

@Entity(value="teams", noClassnameStored = true)
public class TeamObject {

    public TeamObject() {}
    @Id
    private ObjectId id;

    @Indexed(options = @IndexOptions(unique = true))
    private Long LeaderId;
    private ArrayList<Long> listOfMemberIds = new ArrayList<>();

    private long roleID;

    private long guildID;

    @Indexed
    private String name;
    private int color;

    private Long teamChatChannelId;
    private Long teamVoiceChannelId;

    @Reference
    private ArrayList<InviteObject> invites = new ArrayList();

    public String getName() {
        return name;
    }

    public ObjectId getId() {
        return id;
    }

    public ArrayList<Long> getListOfMemberIds() {
        return listOfMemberIds;
    }

    public Color getColor() {
        return new Color(color);
    }

    public long getRoleID() {
        return roleID;
    }

    public Long getLeaderId() {
        return LeaderId;
    }

    public ArrayList<InviteObject> getInvites() {
        return invites;
    }

    public Long getTeamChatChannelId() {
        return teamChatChannelId;
    }

    public Long getTeamVoiceChannelId() {
        return teamVoiceChannelId;
    }

    public void setColor(Color color) {
        this.color = color.getRGB();
    }

    public void setLeaderId(Long leaderId) {
        LeaderId = leaderId;
    }

    public void setListOfMemberIds(ArrayList<Long> listOfMemberIds) {
        this.listOfMemberIds = listOfMemberIds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeamChatChannelId(Long teamChatChannelId) {
        this.teamChatChannelId = teamChatChannelId;
    }

    public void setTeamVoiceChannelId(Long teamVoiceChannelId) {
        this.teamVoiceChannelId = teamVoiceChannelId;
    }

    public void setRoleID(long roleID) {
        this.roleID = roleID;
    }

    public void setInvites(ArrayList<InviteObject> invites) {
        this.invites = invites;
    }

    public void addInvite(InviteObject invite) {
        getInvites().add(invite);
    }

    public void addMember(Long member) {
        this.listOfMemberIds.add(member);
    }

    public void removeMember(Long member) {
        this.listOfMemberIds.remove(member);
    }

    public void setGuildID(long guildID) {
        this.guildID = guildID;
    }

    public long getGuildID() {
        return guildID;
    }
}
