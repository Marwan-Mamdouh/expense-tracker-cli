package com.marwan.dev.expense_tracker.shared.exceptions.budget;

import com.marwan.dev.expense_tracker.shared.exceptions.ErrorMessage;

public class BudgetNotFoundException extends RuntimeException {

  public BudgetNotFoundException() {
    super(ErrorMessage.BUDGET_NOT_FOUND.getMessage());
  }
}