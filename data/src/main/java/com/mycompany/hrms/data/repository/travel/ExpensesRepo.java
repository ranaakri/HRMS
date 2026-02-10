package com.mycompany.hrms.data.repository.travel;

import com.mycompany.hrms.data.entity.travel.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpensesRepo extends JpaRepository<Expenses, Long> {
}
