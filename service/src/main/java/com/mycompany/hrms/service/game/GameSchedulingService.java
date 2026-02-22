package com.mycompany.hrms.service.game;

import com.mycompany.hrms.data.entity.game.*;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.game.*;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class GameSchedulingService {

    private final GameConfigRepo gameConfigRepo;
    private final GameSlotsRepo gameSlotsRepo;
    private final SlotRequestRepo slotRequestRepo;
    private final FinalBookingsRepo finalBookingsRepo;
    private final NotificationService notificationService;
    private final UserGameStatesRepo userGameStatesRepo;
    private final UsersRepo usersRepo;

    @Autowired
    public GameSchedulingService(GameConfigRepo gameConfigRepo,
                                 GameSlotsRepo gameSlotsRepo,
                                 SlotRequestRepo slotRequestRepo,
                                 FinalBookingsRepo finalBookingsRepo,
                                 NotificationService notificationService,
                                 UserGameStatesRepo userGameStatesRepo,
                                 UsersRepo usersRepo){
        this.gameConfigRepo = gameConfigRepo;
        this.gameSlotsRepo = gameSlotsRepo;
        this.slotRequestRepo = slotRequestRepo;
        this.finalBookingsRepo = finalBookingsRepo;
        this.notificationService = notificationService;
        this.userGameStatesRepo = userGameStatesRepo;
        this.usersRepo = usersRepo;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void generateSlotsForToday(){
        List<GameConfig> games = gameConfigRepo.findByIsActiveTrue();
        for(GameConfig game: games){
            LocalDate today = LocalDate.now();
            ZonedDateTime start = ZonedDateTime.of(today, game.getOpenTime(), ZoneId.systemDefault());
            ZonedDateTime close = ZonedDateTime.of(today, game.getCloseTime(), ZoneId.systemDefault());

            while(!start.plusMinutes(game.getSlotDuration()).isAfter(close)){
                GameSlots slot = new GameSlots();
                slot.setStartTime(start);
                slot.setEndTime(start.plusMinutes(game.getSlotDuration()));
                slot.setGameConfig(game);
                slot.setStatus(GameSlots.SlotStatus.OPEN);
                gameSlotsRepo.save(slot);
                start = start.plusMinutes(game.getSlotDuration());
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void confirmUpComingSlots() {
        List<GameSlots> slots = slotRequestRepo.findSlotsStartingSoon();

        for(GameSlots slot : slots) {
            if (slot.getStatus() == GameSlots.SlotStatus.BOOKED) {
                continue;
            }

            List<SlotRequest> slotRequests = slotRequestRepo.findAllByGameSlots_SlotId(slot.getSlotId());
            if (slotRequests == null || slotRequests.isEmpty()) {
                continue;
            }

            SlotRequest selectedRequest = slotRequests.stream()
                    .max(Comparator.comparing(SlotRequest::getGroupAverageScore)
                            .thenComparing(Comparator.comparing(SlotRequest::getRequestId).reversed()))
                    .orElse(null);

            if(selectedRequest == null){
                continue;
            }

            for(SlotRequest req : slotRequests){
                if(req.getRequestId() == selectedRequest.getRequestId()) {
                    req.setStatus(SlotRequest.RequestStatus.APPROVED);
                } else {
                    req.setStatus(SlotRequest.RequestStatus.REJECTED);

                    List<Users> usersList = req.getParticipants().stream().map(RequestParticipants::getUser).toList();
                    notificationService.addNotification(usersList, "SLOT_REJECTED", "Your request for " + slot.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " was not selected");
                }
            }

            slotRequestRepo.saveAll(slotRequests);

            FinalBookings bookings = new FinalBookings();
            bookings.setGameSlot(slot);
            bookings.setConfirmedRequest(selectedRequest);
            bookings.setCompleted(false);
            finalBookingsRepo.save(bookings);

            slot.setStatus(GameSlots.SlotStatus.BOOKED);
            gameSlotsRepo.save(slot);

            List<Long> userIds = slotRequestRepo.findAllParticipantsId(slot.getSlotId(), selectedRequest.getRequestId());

            for(Long userId : userIds){
                userGameStatesRepo.findByUser_UserIdAndGameConfig_GameId(userId, slot.getGameConfig().getGameId())
                        .ifPresent(stats -> {
                            stats.setLastPlayedAt(slot.getStartTime());
                            userGameStatesRepo.save(stats);
                        });
            }

            List<Users> usersList = usersRepo.findAllById(userIds);
            notificationService.addNotification(usersList, "GAME_SLOT_BOOKED",
                    slot.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void makePastGamesCompleted(){
        ZonedDateTime time = ZonedDateTime.now(ZoneId.systemDefault());

        List<FinalBookings> completed = finalBookingsRepo.findPastIncompleteBookings(time);
        if(completed.isEmpty()) {
            return;
        }
        for(FinalBookings bookings : completed){
            bookings.setCompleted(true);
            bookings.getGameSlot().setStatus(GameSlots.SlotStatus.LOCKED);
        }
        finalBookingsRepo.saveAll(completed);
    }
}
