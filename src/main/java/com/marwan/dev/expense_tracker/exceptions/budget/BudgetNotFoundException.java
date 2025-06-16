package com.marwan.dev.expense_tracker.exceptions.budget;

import com.marwan.dev.expense_tracker.exceptions.ErrorMessage;

public class BudgetNotFoundException extends RuntimeException {

  public BudgetNotFoundException() {
    super(ErrorMessage.BUDGET_NOT_FOUND.getMessage());
  }
}