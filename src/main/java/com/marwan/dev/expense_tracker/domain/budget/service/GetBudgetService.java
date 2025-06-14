package com.marwan.dev.expense_tracker.domain.budget.service;

import com.marwan.dev.expense_tracker.CommandInterface;
import com.marwan.dev.expense_tracker.domain.budget.model.Budget;
import com.marwan.dev.expense_tracker.domain.budget.model.dto.SearchArgsForBudget;
import com.marwan.dev.expense_tracker.domain.budget.repository.BudgetRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class GetBudgetService implements CommandInterface<SearchArgsForBudget, Budget> {

  private final BudgetRepository budgetRepository;

  public GetBudgetService(BudgetRepository budgetRepository) {
    this.budgetRepository = budgetRepository;
  }

  @Override
  public Budget execute(SearchArgsForBudget input) {
    if (input.year() == null) {
      return budgetRepository.findByMonthAndYear(input.month(), LocalDate.now().getYear())
          .orElseThrow();
    } else {
      return budgetRepository.findByMonthAndYear(input.month(), input.year()).orElseThrow();
    }
  }
}