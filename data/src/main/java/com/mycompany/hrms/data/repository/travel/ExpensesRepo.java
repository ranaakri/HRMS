package com.mycompany.hrms.data.repository.travel;

import com.mycompany.hrms.data.entity.travel.Expenses;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpensesRepo extends JpaRepository<Expenses, Long> {
    @EntityGraph(attributePaths = {"expensesSplits"})
    List<Expenses> findWithSplitsByTravelDetails_TravelId(long travelId);

    @EntityGraph(attributePaths = {"expensesProofs"})
    List<Expenses> findWithProofsByTravelDetails_TravelId(long travelId);

    @EntityGraph(attributePaths = {"expensesSplits"})
    Optional<Expenses> getExpensesByExpenseId(long expenseId);

    @EntityGraph(attributePaths = {"expensesSplits"})
    List<Expenses> getAllByTravelDetails_TravelIdAndExpensesSplits_TravelingUser_User_UserId(long travelId, long userId);
}
