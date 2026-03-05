package com.mycompany.hrms.data.dtos.travel.response;

import com.mycompany.hrms.data.dtos.travel.request.TravelDetailsReq;

import java.util.List;

public class TravelDetailsRes extends TravelDetailsReq {

    private long travelId;

    private List<TravelGalleryRes> travelGallery;

    private CreatedByUser createdByUser;

    public CreatedByUser getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(CreatedByUser createdBy) {
        this.createdByUser = createdBy;
    }

    public long getTravelId() {
        return travelId;
    }

    public void setTravelId(long travelId) {
        this.travelId = travelId;
    }

    public List<TravelGalleryRes> getTravelGallery() {
        return travelGallery;
    }

    public void setTravelGallery(List<TravelGalleryRes> travelGallery) {
        this.travelGallery = travelGallery;
    }
}
