package com.marwan.dev.expense_tracker.services;

public interface CommandInterface<I, O> {

  O execute(I input);
}