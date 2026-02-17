package com.mycompany.hrms.data.repository.travel;

import com.mycompany.hrms.data.entity.travel.Expenses;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpensesRepo extends JpaRepository<Expenses, Long> {
    @EntityGraph(attributePaths = {"expensesSplits"})
    List<Expenses> findWithSplitsByTravelDetails_TravelId(long travelId);

    @EntityGraph(attributePaths = {"expensesProofs"})
    List<Expenses> findWithProofsByTravelDetails_TravelId(long travelId);

    @EntityGraph(attributePaths = {"expensesSplits"})
    Optional<Expenses> getExpensesByExpenseId(long expenseId);
}
