package com.mycompany.hrms.api.controllers.travel;

import com.mycompany.hrms.api.response.ApiResponse;
import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.service.dtos.travel.request.AddExpense;
import com.mycompany.hrms.service.dtos.travel.request.UpdateExpenseStatus;
import com.mycompany.hrms.service.dtos.travel.response.ExpenseRes;
import com.mycompany.hrms.service.travel.IExpenseService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travel/expense")
public class ExpensesController {

    private final IExpenseService expenseService;

    @Autowired
    public ExpensesController(IExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @GetMapping("/{travelId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<ApiResponse<List<ExpenseRes>>> getListOfExpenseByTravelId(@PathVariable long travelId){
        return ResponseEntity.ok(ApiResponse.success(expenseService.getAllExpenseByTravelId(travelId), "List of expenses fetched successfully"));
    }

    @GetMapping("/expenseId/{expenseId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<ApiResponse<ExpenseRes>> getExpenseByExpenseId(@PathVariable long expenseId){
        return ResponseEntity.ok(ApiResponse.success(expenseService.getExpenseByExpenseId(expenseId), "Expense fetched successfully"));
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<ApiResponse<Long>> addExpense(@RequestBody AddExpense expense){
        long expenseId = expenseService.addExpense(expense);
        return ResponseEntity.ok(ApiResponse.success(expenseId,"New Expense added successfully"));
    }

    @PatchMapping("/{expenseId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee')")
    public ResponseEntity<ApiResponse<String>> updateExpenseStatus(@PathVariable long expenseId, @RequestBody UpdateExpenseStatus expenseStatus){
        Constants.ExpenseStatus status = expenseService.changeExpenseStatus(expenseId, expenseStatus.getStatus());
        return ResponseEntity.ok(ApiResponse.successMsg("Status updated successfully: " + status.toString()));
    }

    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<String>> deleteExpense(@PathVariable long expenseId){
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.ok(ApiResponse.successMsg("Expense deleted successfully"));
    }
}
