package com.marwan.dev.expense_tracker.domain.budget.model.dto;

public record UpdateBudgetRequest(Integer month, Integer year, Double amount) {

}