package com.marwan.dev.expense_tracker.domain.expense.model.dto;

import com.marwan.dev.expense_tracker.domain.expense.model.Expense;

public record CreateExpenseResponse(Expense expense, Double balanceLeft) {

}