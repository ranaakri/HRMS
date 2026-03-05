package com.mycompany.hrms.data.dtos.users.response;

import com.mycompany.hrms.data.dtos.users.request.UserProfileCreate;

public class UserProfileCreated extends UserProfileCreate {
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
