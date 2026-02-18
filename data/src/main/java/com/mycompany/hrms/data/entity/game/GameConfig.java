package com.mycompany.hrms.data.entity.game;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "gameId")
@Entity
public class GameConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long gameId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int minPlayers = 1;

    @Column(nullable = false)
    private int maxPlayers = 2;

    @Column(nullable = false)
    private int slotDuration = 15;

    @Column(nullable = false)
    private ZonedDateTime openTime;

    @Column(nullable = false)
    private ZonedDateTime closeTime;

    @Column(nullable = false)
    private boolean isActive = true;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "gameConfig")
    private Set<UserGameStats> userGameStats;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "gameConfig")
    private Set<GameSlots> gameSlots;
}