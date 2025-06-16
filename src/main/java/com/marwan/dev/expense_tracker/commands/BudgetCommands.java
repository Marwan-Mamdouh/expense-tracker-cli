package com.marwan.dev.expense_tracker.commands;

import com.marwan.dev.expense_tracker.domain.budget.model.dto.SearchArgsForBudget;
import com.marwan.dev.expense_tracker.domain.budget.model.dto.UpdateBudgetRequest;
import com.marwan.dev.expense_tracker.domain.budget.service.GetBudgetService;
import com.marwan.dev.expense_tracker.domain.budget.service.UpdateBudgetService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

/**
 * BudgetCommands provides command-line functionality for setting and retrieving monthly budgets. It
 * uses Spring Shell to expose CLI commands for updating and retrieving budget data.
 */
@Command
public class BudgetCommands {

  private final UpdateBudgetService updateBudgetService;
  private final GetBudgetService getBudgetService;

  /**
   * Constructor for dependency injection.
   *
   * @param updateBudgetService Service to update budget.
   * @param getBudgetService    Service to retrieve budget.
   */
  public BudgetCommands(UpdateBudgetService updateBudgetService,
      GetBudgetService getBudgetService) {
    this.updateBudgetService = updateBudgetService;
    this.getBudgetService = getBudgetService;
  }

  /**
   * Updates the budget amount for a specific month and year.
   *
   * @param amount The new budget amount.
   * @param month  The month (1-12).
   * @param year   The year (2000-2100).
   * @return Confirmation message or error.
   */
  @Command(command = "add-budget", description = "Set a new budget amount for a given month and year.")
  public String addBudget(
      @Option(longNames = "budget", shortNames = 'b', description = "Enter a floating point number.", required = true) Double amount,
      @Option(longNames = "month", shortNames = 'm', description = "Month (1-12)", required = true) @Min(1) @Max(12) Integer month,
      @Option(longNames = "year", shortNames = 'y', description = "Year (2000-2100)") @Min(2000) @Max(2100) Integer year) {
    if (year == null) {
      year = LocalDate.now().getYear();
    }
    try {
      final var updated = updateBudgetService.execute(new UpdateBudgetRequest(month, year, amount))
          .getAmount();
      return String.format("The new budget for %02d/%d is: $%.2f", month, year, updated);
    } catch (RuntimeException e) {
      return "Failed to update budget: " + e.getMessage();
    }
  }

  /**
   * Retrieves the budget for a specific month and year.
   *
   * @param month The month (1-12).
   * @param year  The year (2000-2100).
   * @return Budget amount or error message if not found.
   */
  @Command(command = "get-budget", description = "Show the budget amount for a given month and year.")
  public String getBudget(
      @Option(longNames = "month", shortNames = 'm', description = "Month (1-12)") @Min(1) @Max(12) Integer month,
      @Option(longNames = "year", shortNames = 'y', description = "Year (2000-2100)") @Min(2000) @Max(2100) Integer year) {
    final var localDate = LocalDate.now();
    if (month == null) {
      month = localDate.getMonthValue();
    }
    if (year == null) {
      year = localDate.getYear();
    }
    try {
      final var budget = getBudgetService.execute(new SearchArgsForBudget(month, year)).getAmount();
      return String.format("Current budget for %02d/%d is: $%.2f", month, year, budget);
    } catch (RuntimeException e) {
      return String.format("Budget not found for %02d/%d", month, year);
    }
  }
}