package com.marwan.dev.expense_tracker.exceptions;

public class NotFoundCategoryException extends RuntimeException {

  public NotFoundCategoryException(String wrongCategory) {
    super("Category Not found for: " + wrongCategory);
  }
}
