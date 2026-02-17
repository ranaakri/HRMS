package com.mycompany.hrms.service.travel;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.service.dtos.travel.request.AddExpense;
import com.mycompany.hrms.service.dtos.travel.response.ExpenseRes;

import java.util.List;

public interface IExpenseService {
    long addExpense(AddExpense expense);
    Constants.ExpenseStatus changeExpenseStatus(long expenseId, Constants.ExpenseStatus expenseStatus, String remarks);
    List<ExpenseRes> getAllExpenseByTravelId(long travelId);
    void deleteExpense(long expenseId);
    ExpenseRes getExpenseByExpenseId(long expenseId);
}
