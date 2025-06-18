package com.marwan.dev.expense_tracker.domain.expense.service;

import com.marwan.dev.expense_tracker.domain.expense.model.Category;
import com.marwan.dev.expense_tracker.domain.expense.model.Expense;
import com.marwan.dev.expense_tracker.domain.expense.model.dto.SearchArgsForList;
import com.marwan.dev.expense_tracker.infrastructure.persistence.implementation.ExpenseRepository;
import com.marwan.dev.expense_tracker.shared.CommandInterface;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for retrieving a list of expenses based on optional month and category
 * filters.
 * <p>
 * If no filters are provided (i.e. {@code input} is {@code null}), all expenses will be returned.
 * </p>
 */
@Service
public class ListExpensesService implements CommandInterface<SearchArgsForList, List<Expense>> {

  private final ExpenseRepository expenseRepository;

  /**
   * Constructs a new {@code ListExpensesService} with the specified repository.
   *
   * @param expenseRepository the repository used to fetch expense records
   */
  public ListExpensesService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  /**
   * Executes the operation to retrieve a list of expenses based on the provided filter criteria.
   *
   * @param input search parameters containing optional month and category filters
   * @return a list of matching expenses
   */
  @Override
  public List<Expense> execute(SearchArgsForList input) {
    if (input == null) {
      return expenseRepository.findAll();
    }
    return handleNullArgsHelper(input.month() == null, input.category() == null, input);
  }

  /**
   * Helper method that determines which query to use based on the presence of month and category
   * filters.
   *
   * @param isMonthNull    indicates whether the month filter is null
   * @param isCategoryNull indicates whether the category filter is null
   * @param input          the filter arguments
   * @return a list of expenses matching the specified filters
   */
  private List<Expense> handleNullArgsHelper(boolean isMonthNull, boolean isCategoryNull,
      SearchArgsForList input) {
    if (!isMonthNull && isCategoryNull) {
      return expenseRepository.findByMonth(input.month());
    } else if (!isCategoryNull && isMonthNull) {
      return expenseRepository.findByCategory(Category.from(input.category()));
    } else {
      return expenseRepository.findByMonthAndCategory(input);
    }
  }
}