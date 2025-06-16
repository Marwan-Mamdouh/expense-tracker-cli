package com.marwan.dev.expense_tracker.domain.expense.service;

import com.marwan.dev.expense_tracker.CommandInterface;
import com.marwan.dev.expense_tracker.domain.budget.model.Budget;
import com.marwan.dev.expense_tracker.domain.budget.model.dto.SearchArgsForBudget;
import com.marwan.dev.expense_tracker.domain.budget.service.GetBudgetService;
import com.marwan.dev.expense_tracker.domain.expense.model.Category;
import com.marwan.dev.expense_tracker.domain.expense.model.Expense;
import com.marwan.dev.expense_tracker.domain.expense.model.dto.CreateExpenseRequest;
import com.marwan.dev.expense_tracker.domain.expense.model.dto.CreateExpenseResponse;
import com.marwan.dev.expense_tracker.domain.expense.model.dto.SearchArgsForList;
import com.marwan.dev.expense_tracker.domain.expense.repository.ExpenseRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

/**
 * Service responsible for adding a new expense and calculating the updated budget.
 */
@Service
public class AddExpenseService implements
    CommandInterface<CreateExpenseRequest, CreateExpenseResponse> {

  /**
   * Repository for managing expense data.
   */
  private final ExpenseRepository expenseRepository;

  /**
   * Service for summarizing expenses.
   */
  private final SummaryExpensesService summaryExpensesService;

  /**
   * Service for retrieving budget data.
   */
  private final GetBudgetService getBudgetService;

  /**
   * Constructs an AddExpenseService with the required dependencies.
   *
   * @param expenseRepository      repository to handle expense persistence
   * @param summaryExpensesService service to compute summary of expenses
   * @param getBudgetService       service to retrieve monthly budgets
   */
  public AddExpenseService(ExpenseRepository expenseRepository,
      SummaryExpensesService summaryExpensesService, GetBudgetService getBudgetService) {
    this.expenseRepository = expenseRepository;
    this.summaryExpensesService = summaryExpensesService;
    this.getBudgetService = getBudgetService;
  }

  /**
   * Executes the creation of a new expense and returns a response with remaining budget.
   *
   * @param input the request containing new expense data
   * @return response including the saved expense and remaining budget
   */
  @Override
  public CreateExpenseResponse execute(final CreateExpenseRequest input) {
    final var expense = saveExpense(input);
    final var localDate = LocalDate.now();
    final var budget = fetchBudgetForDate(localDate);
    final var spentAmount = calculateMonthlySummary(localDate);
    return new CreateExpenseResponse(expense, budget.getAmount() - spentAmount);
  }

  /**
   * Saves the expense to the repository.
   *
   * @param input user input with expense details
   * @return the stored Expense object
   */
  private Expense saveExpense(final CreateExpenseRequest input) {
    return expenseRepository.save(
        new Expense(input.description(), input.amount(), Category.from(input.category())));
  }

  /**
   * Fetches the budget for the month and year of the given date.
   *
   * @param localDate the date used to determine month and year
   * @return corresponding budget entry
   */
  private Budget fetchBudgetForDate(final LocalDate localDate) {
    return getBudgetService.execute(
        new SearchArgsForBudget(localDate.getMonthValue(), localDate.getYear()));
  }

  /**
   * Calculates the total expenses for the given month.
   *
   * @param localDate the date used to filter expenses by month
   * @return total amount spent in that month
   */
  private Double calculateMonthlySummary(final LocalDate localDate) {
    return summaryExpensesService.execute(new SearchArgsForList(localDate.getMonthValue(), null));
  }
}