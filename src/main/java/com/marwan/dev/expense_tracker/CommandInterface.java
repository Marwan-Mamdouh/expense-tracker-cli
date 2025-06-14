package com.marwan.dev.expense_tracker;

public interface CommandInterface<I, O> {

  O execute(I input);
}