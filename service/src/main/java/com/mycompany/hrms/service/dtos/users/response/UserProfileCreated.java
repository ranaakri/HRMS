package com.mycompany.hrms.service.dtos.users.response;

import com.mycompany.hrms.service.dtos.users.request.UserProfileCreate;

public class UserProfileCreated extends UserProfileCreate {
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
