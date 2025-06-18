package com.marwan.dev.expense_tracker.domain.expense.service;

import com.marwan.dev.expense_tracker.domain.expense.model.Category;
import com.marwan.dev.expense_tracker.domain.expense.model.Expense;
import com.marwan.dev.expense_tracker.domain.expense.model.dto.SearchArgsForList;
import com.marwan.dev.expense_tracker.domain.expense.repository.ExpenseRepositoryI;
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

  private final ExpenseRepositoryI expenseRepository;

  /**
   * Constructs a new {@code ListExpensesService} with the specified repository.
   *
   * @param expenseRepository the repository used to fetch expense records
   */
  public ListExpensesService(ExpenseRepositoryI expenseRepository) {
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
    return handleNullArgsHelper(input);
  }

  /**
   * Helper method that determines which query to use based on the presence of month and category
   * filters.
   *
   * @param input the filter arguments
   * @return a list of expenses matching the specified filters
   */
  private List<Expense> handleNullArgsHelper(SearchArgsForList input) {
    final boolean isMonthNull = input.month() == null;
    final boolean isCategoryNull = input.category() == null;
    if (!isMonthNull && isCategoryNull) {
      return expenseRepository.findByMonth(input.month());
    } else if (!isCategoryNull && isMonthNull) {
      return expenseRepository.findByCategory(Category.from(input.category()));
    } else {
      return expenseRepository.findByMonthAndCategory(input.month(),
          Category.from(input.category()));
    }
  }
}