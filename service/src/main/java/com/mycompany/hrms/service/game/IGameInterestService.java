package com.mycompany.hrms.service.game;

public interface IGameInterestService {
    void addGameInterest(long userId, long gameId);
    void removeGameInterest(long userId, long gameId);
}
