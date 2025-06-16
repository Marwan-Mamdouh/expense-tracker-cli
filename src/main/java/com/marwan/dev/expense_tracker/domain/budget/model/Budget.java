package com.marwan.dev.expense_tracker.domain.budget.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.marwan.dev.expense_tracker.domain.budget.model.dto.UpdateBudgetRequest;
import lombok.Data;

/**
 * Represents a monthly budget configuration with amount, month, and year.
 */
@Data
public class Budget {

  /**
   * The budget amount for a specific month.
   */
  private Double amount;

  /**
   * The month of the budget (1 - 12).
   */
  private Integer month;

  /**
   * The year of the budget.
   */
  private Integer year;

  /**
   * Constructor for deserializing a Budget from JSON.
   *
   * @param amount the budget amount
   * @param month  the month (1-12)
   * @param year   the year
   */
  @JsonCreator
  public Budget(@JsonProperty("amount") Double amount, @JsonProperty("month") Integer month,
      @JsonProperty("year") Integer year) {
    this.amount = amount;
    this.month = month;
    this.year = year;
  }

  /**
   * Factory method to create a Budget from an UpdateBudgetRequest DTO.
   *
   * @param budgetRequest the request DTO
   * @return a Budget instance
   */
  public static Budget fromUpdateBudgetRequest(UpdateBudgetRequest budgetRequest) {
    if (budgetRequest == null) {
      throw new IllegalArgumentException("budgetRequest must not be null");
    }
    return new Budget(budgetRequest.amount(), budgetRequest.month(), budgetRequest.year());
  }
}