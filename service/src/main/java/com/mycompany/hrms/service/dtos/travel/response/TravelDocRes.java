package com.mycompany.hrms.service.dtos.travel.response;

import com.mycompany.hrms.data.constant.Constants;

import java.time.ZonedDateTime;

public class TravelDocRes {
    private long docId;

    private String filePath;

    private Constants.DocType docType;

    private Constants.DocStatus staus;

    private boolean isLocked;

    private ZonedDateTime uploadedAt;

    private CreatedByUser uploadedBy;

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

    public Constants.DocStatus getStaus() {
        return staus;
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

    public CreatedByUser getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(CreatedByUser uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
