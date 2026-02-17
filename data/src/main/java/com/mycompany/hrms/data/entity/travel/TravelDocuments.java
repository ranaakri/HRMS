package com.mycompany.hrms.data.entity.travel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Date;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "departmentId")
@Entity
public class TravelDocuments {

    public TravelDocuments(){
        this.uploadedAt = ZonedDateTime.now();
        this.staus = Constants.DocStatus.PENDING;
        this.isLocked = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long docId;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private Constants.DocType docType;

    @Column(nullable = false)
    private Constants.DocStatus staus;

    @Column(nullable = false)
    private boolean isLocked;

    @Column(nullable = false)
    private ZonedDateTime uploadedAt;

    @Column(nullable = false)
    private String publicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelingUserId")
    private TravelingUser travelingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users uploadedBy;

    public long getDocId() {
        return docId;
    }

    public void setDocId(long docId) {
        this.docId = docId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Constants.DocType getDocType() {
        return docType;
    }

    public void setDocType(Constants.DocType docType) {
        this.docType = docType;
    }

    public String getStaus() {
        return staus.toString();
    }

    public void setStaus(Constants.DocStatus staus) {
        this.staus = staus;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public ZonedDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(ZonedDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public TravelingUser getTravelingUser() {
        return travelingUser;
    }

    public void setTravelingUser(TravelingUser travelingUser) {
        this.travelingUser = travelingUser;
    }

    public Users getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(Users uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
}
