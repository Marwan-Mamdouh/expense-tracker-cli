package com.marwan.dev.expense_tracker.domain.expense.model.dto;

public record CreateExpenseRequest(String description, Double amount, String category) {

}