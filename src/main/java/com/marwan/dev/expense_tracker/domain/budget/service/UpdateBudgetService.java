package com.marwan.dev.expense_tracker.domain.budget.service;

import com.marwan.dev.expense_tracker.CommandInterface;
import com.marwan.dev.expense_tracker.domain.budget.model.Budget;
import com.marwan.dev.expense_tracker.domain.budget.model.dto.UpdateBudgetRequest;
import com.marwan.dev.expense_tracker.domain.budget.repository.BudgetRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateBudgetService implements CommandInterface<UpdateBudgetRequest, Budget> {

  private final BudgetRepository budgetRepository;

  public UpdateBudgetService(BudgetRepository budgetRepository) {
    this.budgetRepository = budgetRepository;
  }

  @Override
  public Budget execute(UpdateBudgetRequest input) {
    return budgetRepository.save(Budget.fromUpdateBudgetRequest(input));
  }
}