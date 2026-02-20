package com.mycompany.hrms.service.game;

import com.mycompany.hrms.service.dtos.game.response.UserPriorityRes;

import java.util.List;

public interface IBookingService {

    void bookSlot(Long slotId,Long requestedBy, List<Long> userIds);
    List<UserPriorityRes> getPriorityList(long slotId);
}
