package com.marwan.dev.expense_tracker.domain.expense.service;

import com.marwan.dev.expense_tracker.domain.expense.model.Category;
import com.marwan.dev.expense_tracker.domain.expense.model.dto.SearchArgsForList;
import com.marwan.dev.expense_tracker.domain.expense.repository.ExpenseRepositoryI;
import com.marwan.dev.expense_tracker.shared.CommandInterface;
import org.springframework.stereotype.Service;

/**
 * Service responsible for summarizing expenses based on optional month and/or category filters.
 */
@Service
public class SummaryExpensesService implements CommandInterface<SearchArgsForList, Double> {

  /**
   * Repository for managing expense data.
   */
  private final ExpenseRepositoryI expenseRepository;

  /**
   * Constructs a SummaryExpensesService with the provided repository.
   *
   * @param expenseRepository repository used to perform summary queries
   */
  public SummaryExpensesService(ExpenseRepositoryI expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  /**
   * Executes the summary calculation based on the given input arguments.
   *
   * @param input optional filters including month and category
   * @return the total expense amount matching the filter
   */
  @Override
  public Double execute(SearchArgsForList input) {
    if (input == null) {
      return expenseRepository.summeryAll();
    }
    return handleNullArgsHelper(input);
  }

  /**
   * Handles different combinations of null values for month and category to perform appropriate
   * summary calculations.
   *
   * @param input the filtering arguments
   * @return calculated expense summary based on filters
   */
  private Double handleNullArgsHelper(SearchArgsForList input) {
    final boolean isMonthNull = input.month() == null;
    final boolean isCategoryNull = input.category() == null;
    if (!isMonthNull && isCategoryNull) {
      return expenseRepository.summeryByMonth(input.month());
    } else if (!isCategoryNull && isMonthNull) {
      return expenseRepository.summeryByCategory(Category.from(input.category()));
    } else {
      return expenseRepository.summeryByMonthAndCategory(input.month(),
          Category.from(input.category()));
    }
  }
}