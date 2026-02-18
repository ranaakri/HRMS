package com.mycompany.hrms.data.entity.post;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "warningId")
@Entity
public class Warnings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long warningId;

    private String reason;

    private ZonedDateTime warningDate;

    private String entityType;

    private long entityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warnedBy")
    private Users warnedBy;

    public long getWarningId() {
        return warningId;
    }

    public void setWarningId(long warningId) {
        this.warningId = warningId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ZonedDateTime getWarningDate() {
        return warningDate;
    }

    public void setWarningDate(ZonedDateTime warningDate) {
        this.warningDate = warningDate;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public Users getWarnedBy() {
        return warnedBy;
    }

    public void setWarnedBy(Users warnedBy) {
        this.warnedBy = warnedBy;
    }
}
