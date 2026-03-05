package com.mycompany.hrms.service.game;

import com.mycompany.hrms.data.dtos.game.response.GameStatusResponse;
import com.mycompany.hrms.data.dtos.game.response.UserPriorityRes;
import com.mycompany.hrms.data.dtos.travel.response.CreatedByUser;

import java.util.List;

public interface IBookingService {

    void bookSlot(Long slotId, Long requestedBy, List<Long> userIds);

    List<UserPriorityRes> getPriorityList(long slotId);

    void cancelSlotRequest(long slotId, long requestedBy);

    boolean checkBooking(long slotId, long userId);

    GameStatusResponse getBookingStatus(long slotId, long userId);

    List<CreatedByUser> getBookingPartners(long userId, long slotId);
}
