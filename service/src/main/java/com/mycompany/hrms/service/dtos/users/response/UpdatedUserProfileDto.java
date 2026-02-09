package com.mycompany.hrms.service.dtos.users.response;

import com.mycompany.hrms.service.dtos.users.request.UpdateUserProfileDto;

public class UpdatedUserProfileDto extends UpdateUserProfileDto {
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
