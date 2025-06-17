package com.marwan.dev.expense_tracker.domain.expense.service;

import com.marwan.dev.expense_tracker.infrastructure.persistence.implementation.ExpenseRepository;
import com.marwan.dev.expense_tracker.shared.CommandInterface;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for deleting an expense by its ID.
 * <p>
 * Implements {@link CommandInterface} to perform a deletion operation and return a simple
 * confirmation message.
 * </p>
 */
@Service
public class DeleteExpenseService implements CommandInterface<Integer, String> {

  private final ExpenseRepository expenseRepository;

  /**
   * Constructs a new {@code DeleteExpenseService} with the specified repository.
   *
   * @param expenseRepository the repository used to delete expense records
   */
  public DeleteExpenseService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  /**
   * Deletes an expense by its ID.
   *
   * @param input the ID of the expense to delete
   * @return a confirmation message indicating successful deletion
   * @throws RuntimeException if an error occurs during the deletion process
   */
  @Override
  public String execute(Integer input) {
    try {
      expenseRepository.deleteById(input);
      return "expense deleted.";
    } catch (RuntimeException e) {
      throw new RuntimeException(e);
    }
  }
}