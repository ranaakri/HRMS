package com.mycompany.hrms.service.game;

import com.mycompany.hrms.data.entity.game.SlotRequest;
import com.mycompany.hrms.service.dtos.game.response.UserPriorityRes;
import com.mycompany.hrms.service.dtos.travel.response.CreatedByUser;

import java.util.List;

public interface IBookingService {

    void bookSlot(Long slotId, Long requestedBy, List<Long> userIds);

    List<UserPriorityRes> getPriorityList(long slotId);

    void cancelSlotRequest(long slotId, long requestedBy);

    boolean checkBooking(long slotId, long userId);

    SlotRequest.RequestStatus getBookingStatus(long slotId, long userId);

    List<CreatedByUser> getBookingPartners(long userId, long slotId);
}
