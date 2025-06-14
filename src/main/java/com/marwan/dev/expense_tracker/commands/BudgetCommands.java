package com.marwan.dev.expense_tracker.commands;

import com.marwan.dev.expense_tracker.domain.budget.model.dto.SearchArgsForBudget;
import com.marwan.dev.expense_tracker.domain.budget.model.dto.UpdateBudgetRequest;
import com.marwan.dev.expense_tracker.domain.budget.service.GetBudgetService;
import com.marwan.dev.expense_tracker.domain.budget.service.UpdateBudgetService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command
public class BudgetCommands {

  private final UpdateBudgetService updateBudgetService;
  private final GetBudgetService getBudgetService;

  public BudgetCommands(UpdateBudgetService updateBudgetService,
      GetBudgetService getBudgetService) {
    this.updateBudgetService = updateBudgetService;
    this.getBudgetService = getBudgetService;
  }

  @Command(command = "update-amount", description = "set a new amount.")
  public String updateBudget(
      @Option(longNames = "budget", shortNames = 'b', description = "enter a floating point number.") Double amount,
      @Option(longNames = "month", shortNames = 'm', description = "a month to search with") @Max(12) @Min(1) Integer month,
      @Option(longNames = "year", shortNames = 'y', description = "a year to search with") @Min(2000) @Max(2100) Integer year) {
    return String.format("the new amount is: %s",
        updateBudgetService.execute(new UpdateBudgetRequest(month, year, amount)));
  }

  @Command(command = "get-budget", description = "show current amount")
  public String getBudget(
      @Option(longNames = "month", shortNames = 'm', description = "a month to search with") @Max(12) @Min(1) Integer month,
      @Option(longNames = "year", shortNames = 'y', description = "a year to search with") @Min(2000) @Max(2100) Integer year) {
    try {
      return String.format("current budget for this month is: %s",
          getBudgetService.execute(new SearchArgsForBudget(month, year)));
    } catch (RuntimeException e) {
      return String.format("Budget not found for month: %s, year: %s", month, year);
    }
  }
}