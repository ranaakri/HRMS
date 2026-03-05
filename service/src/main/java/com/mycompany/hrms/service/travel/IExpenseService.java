package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.dtos.travel.request.AddExpense;
import com.mycompany.hrms.data.dtos.travel.response.ExpenseRes;

import java.time.ZonedDateTime;
import java.util.List;

public interface IExpenseService {
    long addExpense(AddExpense expense);
    Constants.ExpenseStatus changeExpenseStatus(long expenseId, Constants.ExpenseStatus expenseStatus, String remarks);
    List<ExpenseRes> getAllExpenseByTravelId(long travelId);
    List<ExpenseRes> getAllExpenseByTravelIdFiltered(long travelId, ZonedDateTime startDate, ZonedDateTime endDate);
    void deleteExpense(long expenseId);
    ExpenseRes getExpenseByExpenseId(long expenseId);
    List<ExpenseRes> getMyExpenses(long travelId, long userId);
}
