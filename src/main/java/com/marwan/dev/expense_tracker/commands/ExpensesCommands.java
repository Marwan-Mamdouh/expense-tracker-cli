package com.marwan.dev.expense_tracker.commands;

import com.marwan.dev.expense_tracker.domain.expense.model.Expense;
import com.marwan.dev.expense_tracker.domain.expense.model.dto.CreateExpenseRequest;
import com.marwan.dev.expense_tracker.domain.expense.model.dto.SearchArgsForList;
import com.marwan.dev.expense_tracker.domain.expense.service.AddExpenseService;
import com.marwan.dev.expense_tracker.domain.expense.service.DeleteExpenseService;
import com.marwan.dev.expense_tracker.domain.expense.service.ListExpensesService;
import com.marwan.dev.expense_tracker.domain.expense.service.SummaryExpensesService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.time.Month;
import java.util.List;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

/**
 * ExpenseCommands provides CLI commands for managing expenses, including adding, deleting, listing,
 * and summarizing expense records.
 */
@Command
public class ExpensesCommands {

  private static final String TABLE_HEADER = String.format("%-4s %-12s %-12s %-20s %10s%n", "ID",
      "Date", "Category", "Description", "Amount");

  private final AddExpenseService addExpenseService;
  private final DeleteExpenseService deleteExpenseService;
  private final ListExpensesService listExpenseService;
  private final SummaryExpensesService summaryExpenseService;

  /**
   * Constructs an ExpensesCommands object with injected services.
   *
   * @param addExpenseService      Service for adding new expenses.
   * @param deleteExpenseService   Service for deleting existing expenses.
   * @param listAllExpenseService  Service for listing expenses.
   * @param summeryExpensesService Service for summarizing expenses.
   */
  public ExpensesCommands(AddExpenseService addExpenseService,
      DeleteExpenseService deleteExpenseService, ListExpensesService listAllExpenseService,
      SummaryExpensesService summeryExpensesService) {
    this.addExpenseService = addExpenseService;
    this.deleteExpenseService = deleteExpenseService;
    this.listExpenseService = listAllExpenseService;
    this.summaryExpenseService = summeryExpensesService;
  }

  /**
   * Adds a new expense.
   *
   * @param description Description of the expense
   * @param amount      Amount of the expense
   * @param category    Expense category
   * @return Success message with ID and budget warning if applicable
   */
  @Command(command = "add", description = "Add a new expense entry.")
  public String addExpense(
      @Option(longNames = "description", shortNames = 'd', required = true, description = "Describe your the expense") String description,
      @Option(longNames = "amount", shortNames = 'a', required = true, description = "Expense amount (must be positive)") double amount,
      @Option(longNames = "category", shortNames = 'c', required = true, description =
          "Category of the expense, categories are {FOOD, FRUITS, INTERNET_BILL, TELEPHONE_BILL, "
              + "ELECTRICITY_BILL, WATER_bill, GAS_BILL, CLEANING_AND_GARBAGE, DEBTS, OTHER}") String category) {
    try {
      final var expenseResponse = addExpenseService.execute(
          new CreateExpenseRequest(description, amount, category));
      if (expenseResponse.balanceLeft() > 0) {
        return String.format("Expense added successfully (ID: %d)",
            expenseResponse.expense().getId());
      } else {
        return String.format(
            "Expense added successfully (ID: %d) - Warning: Budget exceeded by $%.2f",
            expenseResponse.expense().getId(), expenseResponse.balanceLeft());
      }
    } catch (IllegalArgumentException e) {
      return "Invalid input: " + e.getMessage();
    } catch (Exception e) {
      return "An unexpected error occurred while adding the expense: " + e.getMessage();
    }
  }

  /**
   * Deletes an expense by ID.
   *
   * @param id ID of the expense to delete
   * @return Success message
   */
  @Command(command = "delete", description = "delete expense by id")
  public String deleteExpense(
      @Option(longNames = "id", shortNames = 'i', description = "id to the expense") @Positive Integer id) {
    deleteExpenseService.execute(id);
    return "Expense deleted successfully";
  }

  /**
   * Lists expenses by optional month and/or category.
   *
   * @param month    Optional month (1-12)
   * @param category Optional category
   * @return Table of expenses or not-found message
   */
  @Command(command = "list", description = "list expenses")
  public String listExpenses(
      @Option(longNames = "month", shortNames = 'm', description = "Enter a month to search") @Min(1) @Max(12) Integer month,
      @Option(longNames = "category", shortNames = 'c', description = "Enter a category to filter with") String category) {
    final List<Expense> expenses = listExpenseService.execute(
        new SearchArgsForList(month, category));
    if (expenses.isEmpty()) {
      return "No expenses found.";
    }
    final StringBuilder table = tableHeader();
    expenses.forEach(e -> table.append(e).append(System.lineSeparator()));
    return table.toString();
  }

  /**
   * Summarizes expenses by optional month and/or category.
   *
   * @param month    Optional month
   * @param category Optional category
   * @return Summary message
   */
  @Command(command = "summary", description = "get summary, enter a month summary of")
  public String summaryExpenses(
      @Option(longNames = "month", shortNames = 'm', description = "Enter a month to search") @Min(1) @Max(12) Integer month,
      @Option(longNames = "category", shortNames = 'c', description = "Enter a category to search") String category) {
    final var args = new SearchArgsForList(month, category);
    return formatSummaryResult(summaryExpenseService.execute(args), args);
  }

  private StringBuilder tableHeader() {
    return new StringBuilder(TABLE_HEADER);
  }


  private String formatSummaryResult(Double summery, SearchArgsForList args) {
    final boolean isMonthNull = args.month() == null;
    final boolean isCategoryNull = args.category() == null;

    if (isMonthNull && isCategoryNull) {
      return String.format("Total expenses: $%.2f", summery);
    } else if (!isMonthNull && isCategoryNull) {
      return String.format("Total expenses for %s: $%.2f", Month.of(args.month()), summery);
    } else if (isMonthNull) {
      return String.format("Total expenses for %s: $%.2f", args.category(), summery);
    } else {
      return String.format("Total expenses for %s in %s: $%.2f", args.category(),
          Month.of(args.month()), summery);
    }
  }
}