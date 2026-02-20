package com.mycompany.hrms.data.repository.game;

import com.mycompany.hrms.data.entity.game.GameConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameConfigRepo extends JpaRepository<GameConfig, Long> {
    List<GameConfig> findByIsActiveTrue();
}
