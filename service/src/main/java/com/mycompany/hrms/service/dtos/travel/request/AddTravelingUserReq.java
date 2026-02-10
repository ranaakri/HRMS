package com.mycompany.hrms.service.dtos.travel.request;

import java.util.List;

public class AddTravelingUserReq {

    private long travelId;

    private List<TravelingUserReq> users;

    public long getTravelId() {
        return travelId;
    }

    public void setTravelId(long travelId) {
        this.travelId = travelId;
    }

    public List<TravelingUserReq> getUsers() {
        return users;
    }

    public void setUsers(List<TravelingUserReq> users) {
        this.users = users;
    }
}
