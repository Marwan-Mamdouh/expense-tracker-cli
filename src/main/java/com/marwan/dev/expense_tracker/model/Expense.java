package com.marwan.dev.expense_tracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Expense {

  private Integer id;
  private final LocalDate createdAt;
  private LocalDate updatedAt;
  private String description;
  private Double amount;
  private Category category;

  @JsonCreator
  public Expense(@JsonProperty("expenseId") Integer id,
      @JsonProperty("createAt") LocalDate createdAt, @JsonProperty("updatedAt") LocalDate updatedAt,
      @JsonProperty("description") String description, @JsonProperty("amount") Double amount,
      @JsonProperty("category") Category category) {
    this.id = id;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.description = description;
    this.amount = amount;
    this.category = category;
  }

  public Expense(String description, Double amount, Category category) {
    this.id = 0;
    this.createdAt = LocalDate.now();
    this.updatedAt = null;
    this.description = description;
    this.amount = amount;
    this.category = category;
  }

  @Override
  public String toString() {
    return String.format("%-4d %-12s %-12s %-20s %10s", id,
        createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), category,
        truncateDescription(description, 20), String.format("$%.2f", amount));
  }

  // Helper method to truncate long descriptions
  private String truncateDescription(String desc, int maxLength) {
    if (desc.length() <= maxLength) {
      return desc;
    }
    return desc.substring(0, maxLength - 3) + "...";
  }
}