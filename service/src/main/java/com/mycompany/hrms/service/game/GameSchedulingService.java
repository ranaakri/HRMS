package com.mycompany.hrms.service.game;

import com.mycompany.hrms.data.entity.game.*;
import com.mycompany.hrms.data.repository.game.*;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
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

    @Autowired
    public GameSchedulingService(GameConfigRepo gameConfigRepo,
                                 GameSlotsRepo gameSlotsRepo,
                                 SlotRequestRepo slotRequestRepo,
                                 FinalBookingsRepo finalBookingsRepo,
                                 NotificationService notificationService,
                                 UserGameStatesRepo userGameStatesRepo){
        this.gameConfigRepo = gameConfigRepo;
        this.gameSlotsRepo = gameSlotsRepo;
        this.slotRequestRepo = slotRequestRepo;
        this.finalBookingsRepo = finalBookingsRepo;
        this.notificationService = notificationService;
        this.userGameStatesRepo = userGameStatesRepo;
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
        for(GameSlots slot:slots){
            List<SlotRequest> slotRequests = slotRequestRepo.findAllByGameSlots_SlotId(slot.getSlotId());
            SlotRequest selectedRequest = slotRequests.stream()
                    .max(Comparator.comparing(SlotRequest::getGroupAverageScore)
                            .thenComparing(Comparator.comparing(SlotRequest::getRequestId).reversed()))
                    .orElse(null);
            if(selectedRequest == null || finalBookingsRepo.existsByConfirmedRequest_RequestId(selectedRequest.getRequestId())){
                continue;
            }
            slot.setStatus(GameSlots.SlotStatus.BOOKED);
            FinalBookings bookings = new FinalBookings();
            bookings.setGameSlot(slot);
            bookings.setConfirmedRequest(selectedRequest);
            bookings.setCompleted(false);

            finalBookingsRepo.save(bookings);

            List<Long> userIds = slotRequestRepo.findAllParticipantsId(slot.getSlotId(), selectedRequest.getRequestId());

            for(Long userId: userIds){
                UserGameStats stats = userGameStatesRepo.findByUser_UserIdAndGameConfig_GameId(userId, slot.getSlotId())
                        .orElseThrow(() -> new ResourceNotFoundException("Stats not found"));
                stats.setLastPlayedAt(slot.getStartTime());
            }

            notificationService.addNotification(userIds, "GAME_SLOT_BOOKED", slot.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
    }
}
