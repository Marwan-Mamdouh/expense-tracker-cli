package com.marwan.dev.expense_tracker.shared;

public interface CommandInterface<I, O> {

  O execute(I input);
}