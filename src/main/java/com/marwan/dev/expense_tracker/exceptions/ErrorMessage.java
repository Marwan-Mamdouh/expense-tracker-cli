package com.marwan.dev.expense_tracker.exceptions;

import lombok.Getter;

@Getter
public enum ErrorMessage {
  CATEGORY_NOT_FOUND("category not found."), BUDGET_NOT_FOUND("budget not found.");

  private final String message;

  ErrorMessage(String message) {
    this.message = message;
  }
}