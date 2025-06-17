package com.marwan.dev.expense_tracker.domain.budget.service;

import com.marwan.dev.expense_tracker.domain.budget.model.Budget;
import com.marwan.dev.expense_tracker.domain.budget.model.dto.UpdateBudgetRequest;
import com.marwan.dev.expense_tracker.domain.budget.repository.BudgetRepository;
import com.marwan.dev.expense_tracker.shared.CommandInterface;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for updating or creating a {@link Budget} entity using data provided in
 * a {@link UpdateBudgetRequest} DTO.
 *
 * <p>This service delegates persistence to the {@link BudgetRepository}.</p>
 */
@Service
public class UpdateBudgetService implements CommandInterface<UpdateBudgetRequest, Budget> {

  private final BudgetRepository budgetRepository;

  /**
   * Constructs the UpdateBudgetService with the given {@link BudgetRepository}.
   *
   * @param budgetRepository the budget repository used to persist updated budgets
   */
  public UpdateBudgetService(BudgetRepository budgetRepository) {
    this.budgetRepository = budgetRepository;
  }

  /**
   * Executes the update operation by converting the input DTO to a {@link Budget} entity and saving
   * it using the {@link BudgetRepository}.
   *
   * @param input the update request containing new budget data
   * @return the updated or newly created budget entity
   */
  @Override
  public Budget execute(UpdateBudgetRequest input) {
    return budgetRepository.save(Budget.fromUpdateBudgetRequest(input));
  }
}