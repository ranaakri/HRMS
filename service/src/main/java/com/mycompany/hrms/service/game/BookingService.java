package com.mycompany.hrms.service.game;

import com.mycompany.hrms.data.entity.game.*;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.game.*;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.data.dtos.game.response.GameStatusResponse;
import com.mycompany.hrms.data.dtos.game.response.RequestedByUser;
import com.mycompany.hrms.data.dtos.game.response.UserPriorityRes;
import com.mycompany.hrms.data.dtos.travel.response.CreatedByUser;
import com.mycompany.hrms.service.email.EmailService;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import com.mycompany.hrms.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BookingService implements IBookingService {

    private final GameSlotsRepo gameSlotsRepo;
    private final UserGameStatesRepo userGameStatesRepo;
    private final FinalBookingsRepo finalBookingsRepo;
    private final PriorityService priorityService;
    private final UsersRepo usersRepo;
    private final RequestParticipantsRepo requestParticipantsRepo;
    private final SlotRequestRepo slotRequestRepo;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Autowired
    public BookingService(GameSlotsRepo gameSlotsRepo,
                          UserGameStatesRepo userGameStatesRepo,
                          FinalBookingsRepo finalBookingsRepo,
                          PriorityService priorityService,
                          UsersRepo usersRepo,
                          RequestParticipantsRepo requestParticipantsRepo,
                          SlotRequestRepo slotRequestRepo,
                          NotificationService notificationService,
                          ModelMapper modelMapper,
                          EmailService emailService) {
        this.priorityService = priorityService;
        this.finalBookingsRepo = finalBookingsRepo;
        this.gameSlotsRepo = gameSlotsRepo;
        this.userGameStatesRepo = userGameStatesRepo;
        this.usersRepo = usersRepo;
        this.requestParticipantsRepo = requestParticipantsRepo;
        this.slotRequestRepo = slotRequestRepo;
        this.notificationService = notificationService;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    @Transactional
    public void bookSlot(Long slotId, Long requestedBy, List<Long> userIds) {
        GameSlots gameSlots = gameSlotsRepo.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Game slot not found"));

        Users requested = usersRepo.findById(requestedBy)
                .orElseThrow(() -> new ResourceNotFoundException("Requested by user not found"));

        if (ZonedDateTime.now().isAfter(gameSlots.getStartTime().minusMinutes(15))) {
            if (slotRequestRepo.findAllByGameSlots_SlotIdAndStatus(slotId, SlotRequest.RequestStatus.PENDING).isEmpty()
                    && !finalBookingsRepo.existsByGameSlot_SlotId(slotId)) {
                SlotRequest request = new SlotRequest();
                request.setGroupAverageScore(0);
                request.setRequestTimeStamp(ZonedDateTime.now());
                request.setGameSlots(gameSlots);
                request.setRequestBy(requested);
                request.setStatus(SlotRequest.RequestStatus.APPROVED);

                request = slotRequestRepo.save(request);

                List<Users> participants = usersRepo.findUsersByUserIdIn(userIds);

                for (Users p : participants) {
                    RequestParticipants requestParticipants = new RequestParticipants();
                    requestParticipants.setRequest(request);
                    requestParticipants.setUser(p);
                    requestParticipantsRepo.save(requestParticipants);
                }

                FinalBookings bookings = new FinalBookings();
                bookings.setGameSlot(gameSlots);
                bookings.setConfirmedRequest(request);
                bookings.setCompleted(false);
                finalBookingsRepo.save(bookings);

                gameSlots.setStatus(GameSlots.SlotStatus.BOOKED);
                gameSlotsRepo.save(gameSlots);

                List<Long> userIdsForNotification = slotRequestRepo.findAllParticipantsId(gameSlots.getSlotId(), request.getRequestId());

                for (Long userId : userIdsForNotification) {
                    userGameStatesRepo.findByUser_UserIdAndGameConfig_GameId(userId, gameSlots.getGameConfig().getGameId())
                            .ifPresent(stats -> {
                                stats.setLastPlayedAt(gameSlots.getStartTime());
                                userGameStatesRepo.save(stats);
                            });
                }

                List<Users> usersList = usersRepo.findAllById(userIds);
                notificationService.addNotification(usersList, "GAME_SLOT_BOOKED",
                        gameSlots.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                emailService.sendGameSlotConfirmedEmail(usersList);

                return;
            } else
                throw new BadRequestException("Booking closed");
        }

        if (!gameSlots.getStatus().equals(GameSlots.SlotStatus.OPEN)) {
            throw new BadRequestException("Slot is not open");
        }

        if (slotRequestRepo.existsByParticipants_User_UserIdInAndStatusAndRequestTimeStampBefore(userIds, SlotRequest.RequestStatus.PENDING, ZonedDateTime.now()))
            throw new BadRequestException("One or more participants have pending booking");

        if (slotRequestRepo.existsByGameSlots_SlotIdAndParticipants_User_UserIdIn(slotId, userIds)) {
            throw new BadRequestException("Only Single booking allowed per person");
        }

        for (Long id : userIds) {
            if (finalBookingsRepo.existsActiveBooking(id, ZonedDateTime.now())) {
                throw new BadRequestException("User has active booking");
            }
        }

        List<UserGameStats> stats = userGameStatesRepo.findByUser_UserIdInAndGameConfig_GameId(userIds, gameSlots.getGameConfig().getGameId());

        if (stats.size() != userIds.size()) {
            throw new BadRequestException("One or more participants has not configured this game");
        }

        for (UserGameStats s : stats) {
            if (!s.isInterested()) {
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

        for (Users p : participants) {
            RequestParticipants requestParticipants = new RequestParticipants();
            requestParticipants.setUser(p);
            request.addRequestParticipants(requestParticipants);
        }

        slotRequestRepo.save(request);
    }

    private double validateGroupPriority(List<UserGameStats> gameStats) {
        List<Integer> priorities = gameStats.stream().map(priorityService::calculatePriority).toList();
        double average = priorities.stream().mapToInt(i -> i).average().orElse(0);
        int max = Collections.max(priorities);
        int min = Collections.min(priorities);

        if ((max - min) > 40) {
            throw new BadRequestException("Unfair grouping");
        }

        return average;
    }

    public List<UserPriorityRes> getPriorityList(long slotId) {
        if (!gameSlotsRepo.existsById(slotId)) {
            throw new ResourceNotFoundException("Game slot not found");
        }

        List<SlotRequest> slotRequests = slotRequestRepo.findAllByGameSlots_SlotId(slotId)
                .stream()
                .filter(val -> !val.getStatus().equals(SlotRequest.RequestStatus.DELETED)).toList();

        List<UserPriorityRes> response = new ArrayList<>();
        for (SlotRequest s : slotRequests) {
            Users requestedBy = s.getRequestBy();
            response.add(new UserPriorityRes(s.getRequestId(), (int) s.getGroupAverageScore(), new RequestedByUser(requestedBy.getUserId(), requestedBy.getName(), requestedBy.getEmail())));
        }
        return response.stream()
                .sorted(
                        Comparator.comparing(UserPriorityRes::getPriority)
                                .reversed()
                                .thenComparing(UserPriorityRes::getRequestId, Comparator.nullsFirst(Comparator.naturalOrder()))).toList();
    }

    @Transactional
    public void cancelSlotRequest(long slotId, long requestedBy) {
        SlotRequest slotRequest = slotRequestRepo.findByGameSlots_SlotIdAndRequestBy_UserId(slotId, requestedBy)
                .orElseThrow(() -> new ResourceNotFoundException("Slot request not found"));

        if (slotRequest.getStatus().equals(SlotRequest.RequestStatus.APPROVED)) {
            cancelConformedBooking(requestedBy, slotId);
            return;
        }
        slotRequestRepo.delete(slotRequest);

    }

    @Transactional
    public void cancelConformedBooking(long userId, long slotId) {
        FinalBookings finalBookings = finalBookingsRepo.findByUser_UserIdAndGameSlots_SlotId(userId, slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Final booking not found"));

        if (finalBookings.isCompleted()) {
            throw new BadRequestException("Can not cancel Completed Slot");
        }

        SlotRequest oldRequest = finalBookings.getConfirmedRequest();
        GameSlots slot = finalBookings.getGameSlot();

        List<Long> oldUserIds = slotRequestRepo.findAllParticipantsId(slot.getSlotId(), oldRequest.getRequestId());
        List<Users> canceledUsers = usersRepo.findAllById(oldUserIds);
        notificationService.addNotification(canceledUsers, "BOOKING_CANCELED", "Your booking for " + slot.getStartTime() + " has been canceled");
        emailService.sendGameSlotCancelledEmail(canceledUsers);

        oldRequest.setStatus(SlotRequest.RequestStatus.DELETED);

        slotRequestRepo.save(oldRequest);

        finalBookingsRepo.delete(finalBookings);

        List<SlotRequest> pendingRequest = slotRequestRepo.findAllByGameSlots_SlotIdAndStatus(slot.getSlotId(), SlotRequest.RequestStatus.REJECTED);

        SlotRequest nextBestRequest = pendingRequest.stream()
                .max(Comparator.comparing(SlotRequest::getGroupAverageScore)
                        .thenComparing(Comparator.comparing(SlotRequest::getRequestId).reversed()))
                .orElse(null);

        if (nextBestRequest != null) {
            nextBestRequest.setStatus(SlotRequest.RequestStatus.APPROVED);

            slotRequestRepo.save(nextBestRequest);

            FinalBookings newFinalBooking = new FinalBookings();
            newFinalBooking.setGameSlot(slot);
            newFinalBooking.setConfirmedRequest(nextBestRequest);
            newFinalBooking.setCompleted(false);
            finalBookingsRepo.save(newFinalBooking);

            List<Long> userIds = slotRequestRepo.findAllParticipantsId(slot.getSlotId(), nextBestRequest.getRequestId());

            for (Long id : userIds) {
                UserGameStats stats = userGameStatesRepo.findByUser_UserIdAndGameConfig_GameId(id, slot.getGameConfig().getGameId())
                        .orElseThrow(() -> new ResourceNotFoundException("Stats not found exception"));

                stats.setLastPlayedAt(slot.getStartTime());
                userGameStatesRepo.save(stats);
            }

            List<Users> confirmedUsers = usersRepo.findAllById(userIds);
            notificationService.addNotification(confirmedUsers,
                    "GAME_SLOT_BOOKED", "You are bumped up the queue! Slot confirmed for "
                            + slot.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " ");
            emailService.sendGameSlotConfirmedEmail(confirmedUsers);
        } else {
            slot.setStatus(GameSlots.SlotStatus.OPEN);
            gameSlotsRepo.save(slot);
        }
    }

    public boolean checkBooking(long slotId, long userId) {
        SlotRequest request = slotRequestRepo.findByGameSlots_SlotIdAndUser_SlotId(slotId, userId)
                .orElse(null);
        return request != null;
    }

    public GameStatusResponse getBookingStatus(long slotId, long userId) {
        SlotRequest request = slotRequestRepo.findByGameSlots_SlotIdAndUser_SlotId(slotId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("No Status found"));
        return modelMapper.map(request, GameStatusResponse.class);
    }

    public List<CreatedByUser> getBookingPartners(long userId, long slotId) {
        SlotRequest slotRequest = slotRequestRepo.findByGameSlots_SlotIdAndUser_SlotId(slotId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("No slot request found"));

        List<Long> userIds = slotRequestRepo.findAllParticipantsId(slotId, slotRequest.getRequestId());
        if (userIds.isEmpty())
            throw new ResourceNotFoundException("No Game partners found");

        List<Users> users = usersRepo.findAllById(userIds);

        return users.stream().map(val -> modelMapper.map(val, CreatedByUser.class)).toList();
    }
}