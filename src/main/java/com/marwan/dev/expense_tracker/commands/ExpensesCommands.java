package com.marwan.dev.expense_tracker.commands;

import com.marwan.dev.expense_tracker.model.Expense;
import com.marwan.dev.expense_tracker.model.dto.CreateExpenseDTO;
import com.marwan.dev.expense_tracker.model.dto.SearchArgsForList;
import com.marwan.dev.expense_tracker.services.AddExpenseService;
import com.marwan.dev.expense_tracker.services.DeleteExpenseService;
import com.marwan.dev.expense_tracker.services.ListService;
import com.marwan.dev.expense_tracker.services.SummeryService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.time.Month;
import java.util.ArrayList;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command
public class ExpensesCommands {

  private final AddExpenseService addExpenseService;
  private final DeleteExpenseService deleteExpenseService;
  private final ListService listService;
  private final SummeryService summeryService;

  public ExpensesCommands(AddExpenseService addExpenseService,
      DeleteExpenseService deleteExpenseService, ListService listAllExpenseService,
      SummeryService summeryAllExpensesService) {
    this.addExpenseService = addExpenseService;
    this.deleteExpenseService = deleteExpenseService;
    this.listService = listAllExpenseService;
    this.summeryService = summeryAllExpensesService;
  }

  @Command(command = "add", description = "add new expense")
  public String addExpense(
      @Option(longNames = "description", shortNames = 'd', required = true, description = "describe the expense") String description,
      @Option(longNames = "amount", shortNames = 'a', required = true, description = "how much this cost you?") double amount,
      @Option(longNames = "category", shortNames = 'c', required = true, description = "put your "
          + "expenses into a categories for better filtering, categories are {FOOD, FRUITS, "
          + "INTERNET_BILL, TELEPHONE_BILL, ELECTRICITY_BILL, WATER_bill, GAS_BILL, "
          + "CLEANING_AND_GARBAGE, DEBTS, OTHER}") String category) {
    try {
      return String.format("Expense added successfully (ID: %d)",
          addExpenseService.execute(new CreateExpenseDTO(description, amount, category)).getId());
    } catch (Exception e) {
      return e.getMessage();
    }
  }

  @Command(command = "delete", description = "delete expense by id")
  public String deleteExpense(
      @Option(longNames = "id", shortNames = 'i', description = "id to the expense") @Positive Integer id) {
    deleteExpenseService.execute(id);
    return "Expense deleted successfully";
  }

  @Command(command = "list", description = "list expenses")
  public String listExpenses(
      @Option(longNames = "month", shortNames = 'm', description = "Enter a month to search") @Min(1) @Max(12) Integer month,
      @Option(longNames = "category", shortNames = 'c', description = "Enter a category to filter with") String category) {
    final ArrayList<Expense> expenses = listService.execute(new SearchArgsForList(month, category));
    if (expenses.isEmpty()) {
      return "No expenses found.";
    }
    final StringBuilder table = tableHeader();
    for (Expense expense : expenses) {
      table.append(expense.toString()).append("\n");
    }
    return table.toString();
  }

  @Command(command = "summery", description = "get summery, enter a month summery of")
  public String summeryExpenses(
      @Option(longNames = "month", shortNames = 'm', description = "Enter a month to search") @Min(1) @Max(12) Integer month,
      @Option(longNames = "category", shortNames = 'c', description = "Enter a category to search") String category) {
    final var args = new SearchArgsForList(month, category);
    return handleNullArgsHelper(month == null, category == null, summeryService.execute(args),
        args);
  }

  private StringBuilder tableHeader() {
    final StringBuilder result = new StringBuilder();
    result.append(
        String.format("%-4s %-12s %-12s %-20s %10s%n", "ID", "Date", "Category", "Description",
            "Amount"));
    return result;
  }

  private String handleNullArgsHelper(boolean isMonthNull, boolean isCategory, Double summery,
      SearchArgsForList args) {
    if (isMonthNull && isCategory) {
      return String.format("Total expenses: $%.2f", summery);
    } else if (!isMonthNull && isCategory) {
      return String.format("Total expenses for %s: $%.2f", Month.of(args.month()), summery);
    } else if (isMonthNull) {
      return String.format("Total expenses for %s: $%.2f", args.category(), summery);
    } else {
      return String.format("Total expenses for %s in %s: $%.2f", args.category(),
          Month.of(args.month()), summery);
    }
  }
}