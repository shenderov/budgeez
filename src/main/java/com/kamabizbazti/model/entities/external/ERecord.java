package com.kamabizbazti.model.entities.external;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ERecord {

    @NotNull(message = "error.category.id.notnull")
    private Long categoryId;

    @NotNull(message = "error.record.amount.notnull")
    private Double amount;

    @Size(max = 100, message = "error.record.comment.size")
    private String comment;

    @NotNull(message = "error.record.date.notnull")
    private Long date;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ERecord{" +
                "categoryId=" + categoryId +
                ", amount=" + amount +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                '}';
    }
}
