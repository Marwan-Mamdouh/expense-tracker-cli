package com.marwan.dev.expense_tracker.domain.budget.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.marwan.dev.expense_tracker.domain.budget.model.dto.UpdateBudgetRequest;
import java.time.LocalDate;
import lombok.Data;

@Data
public class Budget {

  private Double amount;
  private Integer month;
  private Integer year;

  @JsonCreator
  public Budget(@JsonProperty("amount") Double amount, @JsonProperty("month") Integer month,
      @JsonProperty("year") Integer year) {
    this.amount = amount;
    this.month = month;
    this.year = year;
  }

//  public Budget(Double amount, Integer month) {
//    this.amount = amount;
//    this.month = month;
//    this.year = LocalDate.now().getYear();
//  }

  public static Budget fromUpdateBudgetRequest(UpdateBudgetRequest budgetRequest) {
    return new Budget(budgetRequest.amount(), budgetRequest.month(), budgetRequest.year());
  }
}