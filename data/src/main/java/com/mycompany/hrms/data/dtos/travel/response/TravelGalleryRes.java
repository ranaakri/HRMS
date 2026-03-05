package com.mycompany.hrms.data.dtos.travel.response;

import java.time.ZonedDateTime;

public class TravelGalleryRes {

    private long imageId;

    private String filePath;

    private ZonedDateTime uploadedAt;


    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ZonedDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(ZonedDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}