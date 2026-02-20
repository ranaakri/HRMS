package com.mycompany.hrms.data.repository.game;

import com.mycompany.hrms.data.entity.game.RequestParticipants;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestParticipantsRepo extends JpaRepository<RequestParticipants, Long> {
}
