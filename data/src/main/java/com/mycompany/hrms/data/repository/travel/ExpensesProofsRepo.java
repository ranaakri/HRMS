package com.mycompany.hrms.data.repository.travel;

import com.mycompany.hrms.data.entity.travel.ExpensesProofs;
import com.mycompany.hrms.data.entity.travel.TravelDocuments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpensesProofsRepo extends JpaRepository<ExpensesProofs, Long> {
    List<ExpensesProofs> getExpensesProofsByExpenses_ExpenseId(long expenseId);
}
