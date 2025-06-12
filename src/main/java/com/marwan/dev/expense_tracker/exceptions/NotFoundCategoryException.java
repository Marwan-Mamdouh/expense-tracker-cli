package com.marwan.dev.expense_tracker.exceptions;

public class NotfoundCategoryException extends RuntimeException {
  public NotfoundCategoryException(String message) {
    super(message);
  }
}
