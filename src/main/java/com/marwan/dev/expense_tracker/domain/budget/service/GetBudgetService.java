package com.marwan.dev.expense_tracker.domain.budget.service;

import com.marwan.dev.expense_tracker.domain.budget.model.Budget;
import com.marwan.dev.expense_tracker.domain.budget.model.dto.SearchArgsForBudget;
import com.marwan.dev.expense_tracker.domain.budget.repository.BudgetRepository;
import com.marwan.dev.expense_tracker.shared.CommandInterface;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

/**
 * Service class that provides functionality for retrieving a budget entry based on a specific month
 * and optionally a specific year.
 * <p>
 * If the year is not provided, the current year is used by default.
 * </p>
 */
@Service
public class GetBudgetService implements CommandInterface<SearchArgsForBudget, Budget> {

  private final BudgetRepository budgetRepository;

  /**
   * Constructs the GetBudgetService with a given {@link BudgetRepository}.
   *
   * @param budgetRepository the budget repository used to retrieve budget data
   */
  public GetBudgetService(BudgetRepository budgetRepository) {
    this.budgetRepository = budgetRepository;
  }

  /**
   * Executes the search operation to find a {@link Budget} for the given month and year. If the
   * year is null, the current year will be used.
   *
   * @param input the search parameters containing the month and optionally the year
   * @return the matching budget
   * @throws NoSuchElementException if no budget is found
   */
  @Override
  public Budget execute(SearchArgsForBudget input) {
    final int targetYear = input.year() != null ? input.year() : LocalDate.now().getYear();
    return budgetRepository.findByMonthAndYear(input.month(), targetYear)
        .orElseThrow(NoSuchElementException::new);
  }
}