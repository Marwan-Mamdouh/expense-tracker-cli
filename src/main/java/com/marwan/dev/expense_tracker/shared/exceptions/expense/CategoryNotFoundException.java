package com.marwan.dev.expense_tracker.shared.exceptions.expense;

import com.marwan.dev.expense_tracker.shared.exceptions.ErrorMessage;

public class CategoryNotFoundException extends RuntimeException {

  public CategoryNotFoundException(String wrongCategory) {
    super(ErrorMessage.CATEGORY_NOT_FOUND.getMessage());
  }
}