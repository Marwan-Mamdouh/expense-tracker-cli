package com.marwan.dev.expense_tracker.exceptions.expense;

import com.marwan.dev.expense_tracker.exceptions.ErrorMessage;

public class CategoryNotFoundException extends RuntimeException {

  public CategoryNotFoundException(String wrongCategory) {
    super(ErrorMessage.CATEGORY_NOT_FOUND.getMessage());
  }
}