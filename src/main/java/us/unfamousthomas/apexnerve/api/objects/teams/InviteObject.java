package us.unfamousthomas.apexnerve.api.objects.teams;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

@Entity(value="invites", noClassnameStored = true)
public class InviteObject {
    @Id
    private ObjectId id;

    @Indexed
    boolean active;
    private Long memberSentTo;
    private Long memberSentFrom;
    private Long timeInvitedAt;
    private Long guildId;


    public void setActive(boolean active) {
        this.active = active;
    }

    public void setTimeInvitedAt(Long timeInvitedAt) {
        this.timeInvitedAt = timeInvitedAt;
    }

    public void setMemberSentTo(Long memberSentTo) {
        this.memberSentTo = memberSentTo;
    }

    public void setMemberSentFrom(Long memberSentFrom) {
        this.memberSentFrom = memberSentFrom;
    }

    public Long getMemberSentTo() {
        return memberSentTo;
    }

    public Long getMemberSentFrom() {
        return memberSentFrom;
    }

    public Long getTimeInvitedAt() {
        return timeInvitedAt;
    }

    public boolean isActive() {
        return active;
    }

    public Long getGuildId() {
        return guildId;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }
}
