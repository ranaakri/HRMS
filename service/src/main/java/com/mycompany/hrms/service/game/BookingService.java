package com.mycompany.hrms.service.game;

import com.mycompany.hrms.data.entity.game.*;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.game.*;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.dtos.game.response.RequestedByUser;
import com.mycompany.hrms.service.dtos.game.response.UserPriorityRes;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class BookingService implements IBookingService {

    private final GameSlotsRepo gameSlotsRepo;
    private final UserGameStatesRepo userGameStatesRepo;
    private final FinalBookingsRepo finalBookingsRepo;
    private final PriorityService priorityService;
    private final UsersRepo usersRepo;
    private final RequestParticipantsRepo requestParticipantsRepo;
    private final SlotRequestRepo slotRequestRepo;

    @Autowired
    public BookingService(GameSlotsRepo gameSlotsRepo,
                          UserGameStatesRepo userGameStatesRepo,
                          FinalBookingsRepo finalBookingsRepo,
                          PriorityService priorityService,
                          UsersRepo usersRepo,
                          RequestParticipantsRepo requestParticipantsRepo,
                          SlotRequestRepo slotRequestRepo){
        this.priorityService = priorityService;
        this.finalBookingsRepo = finalBookingsRepo;
        this.gameSlotsRepo = gameSlotsRepo;
        this.userGameStatesRepo = userGameStatesRepo;
        this.usersRepo = usersRepo;
        this.requestParticipantsRepo = requestParticipantsRepo;
        this.slotRequestRepo = slotRequestRepo;
    }

    public void bookSlot(Long slotId, Long requestedBy, List<Long> userIds){
        GameSlots gameSlots = gameSlotsRepo.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Game slot not found"));

        Users requested = usersRepo.findById(requestedBy)
                .orElseThrow(() -> new ResourceNotFoundException("Requested by user not found"));

        if(ZonedDateTime.now().isAfter(gameSlots.getStartTime().minusMinutes(15))){
            throw new BadRequestException("Booking closed");
        }

        if(!gameSlots.getStatus().equals(GameSlots.SlotStatus.OPEN)){
            throw new BadRequestException("Slot is not open");
        }

        if(slotRequestRepo.existsByGameSlots_SlotIdAndParticipants_User_UserIdIn(slotId, userIds)){
            throw new BadRequestException("Only Single booking allowed per person");
        }

        for(Long id: userIds){
            if(finalBookingsRepo.existsActiveBooking(id, ZonedDateTime.now())){
                throw new BadRequestException("User has active booking");
            }
        }

        if(!userGameStatesRepo.existsByUser_UserIdInAndGameConfig_GameId(userIds, gameSlots.getGameConfig().getGameId())){
            throw new BadRequestException("some one has not configured game as interested");
        }

        List<UserGameStats> stats = userGameStatesRepo.findByUser_UserIdInAndGameConfig_GameId(userIds, gameSlots.getGameConfig().getGameId());

        for(UserGameStats s : stats){
            if(!s.isInterested()){
                throw new BadRequestException(s.getUser().getName() + " is not interested in game.");
            }
        }

        double average = validateGroupPriority(stats);

        SlotRequest request = new SlotRequest();
        request.setGroupAverageScore(average);
        request.setRequestTimeStamp(ZonedDateTime.now());
        request.setGameSlots(gameSlots);
        request.setRequestBy(requested);

        List<Users> participants = usersRepo.findUsersByUserIdIn(userIds);

        for (Users p : participants){
            RequestParticipants requestParticipants = new RequestParticipants();
            requestParticipants.setRequest(request);
            requestParticipants.setUser(p);
            requestParticipantsRepo.save(requestParticipants);
        }
    }

    private double validateGroupPriority(List<UserGameStats> gameStats){
        List<Integer> priorities = gameStats.stream().map(priorityService::calculatePriority).toList();
        double average = priorities.stream().mapToInt(i->i).average().orElse(0);
        int max = Collections.max(priorities);
        int min = Collections.min(priorities);

        if(average < 30){
            throw new BadRequestException("Priority is too low");
        }
        if((max-min) > 40){
            throw new BadRequestException("Unfair grouping");
        }

        return average;
    }

    public List<UserPriorityRes> getPriorityList(long slotId){
        if(!gameSlotsRepo.existsById(slotId)){
            throw new ResourceNotFoundException("Game slot not found");
        }

        List<SlotRequest> slotRequests = slotRequestRepo.findAllByGameSlots_SlotId(slotId);


        List<UserPriorityRes> response = new ArrayList<>();
        for(SlotRequest s : slotRequests){
            Users requestedBy = s.getRequestBy();
            response.add(new UserPriorityRes(s.getRequestId(),(int)s.getGroupAverageScore(), new RequestedByUser(requestedBy.getUserId(), requestedBy.getName(), requestedBy.getEmail())));
        }
        return response.stream().sorted(
                Comparator.comparing(UserPriorityRes::getPriority)
                .reversed()
                .thenComparing(UserPriorityRes::getRequestId, Comparator.nullsFirst(Comparator.naturalOrder()))).toList();
    }
}