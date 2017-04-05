package com.kamabizbazti.model.entities.dao;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamabizbazti.model.entities.external.ERecord;
import com.kamabizbazti.model.enumerations.CategoryType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@SuppressWarnings({"UnusedDeclaration"})
@Entity
@Table(name = "Record")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "record_seq_gen")
    @SequenceGenerator(name = "record_seq_gen", sequenceName = "RECORD_SEQ")
    @Column(name = "recordId", nullable = false)
    private long recordId;

    @ManyToOne()
    @JoinColumn(name = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User userId;

    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "categoryId")
    private GeneralCategory category;

    @Column(name = "categoryType", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private CategoryType categoryType;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "comment")
    @Size(max = 100)
    private String comment;

    @Column(name = "date", nullable = false)
    private Long date;

    public Record() {
        super();
    }

    public Record(User userId, GeneralCategory category, Double amount, Long date) {
        super();
        this.userId = userId;
        this.category = category;
        this.categoryType = category.getType();
        this.amount = amount;
        this.date = date;
    }

    public Record(ERecord record) {
        super();
        //this.category = record.getCategory();
        //this.categoryType = category.getType();
        this.amount = record.getAmount();
        this.date = record.getDate();
        this.comment = record.getComment();
    }

//public Record(GeneralPurpose purpose, double amount, String date) throws ParseException {
//	super();
//	ISO8601DateFormat df = new ISO8601DateFormat();
//	//this.userId = userId;
//	this.purpose = purpose;
//	this.purposeType = purpose.getType();
//	this.amount = amount;
//	this.date = df.parse(date).getTime();
//}

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public long getRecordId() {
        return recordId;
    }

    public User getUserId() {
        return userId;
    }

    public GeneralCategory getCategory() {
        return category;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCategory(GeneralCategory category) {
        this.category = category;
        this.categoryType = category.getType();
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        if (recordId != record.recordId) return false;
        if (userId != null ? !userId.equals(record.userId) : record.userId != null) return false;
        if (category != null ? !category.equals(record.category) : record.category != null) return false;
        if (categoryType != record.categoryType) return false;
        if (amount != null ? !amount.equals(record.amount) : record.amount != null) return false;
        if (comment != null ? !comment.equals(record.comment) : record.comment != null) return false;
        return date != null ? date.equals(record.date) : record.date == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (recordId ^ (recordId >>> 32));
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (categoryType != null ? categoryType.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Record [recordId=" + recordId + ", userId=" + userId + ", category=" + category + ", categoryType=" + categoryType
                + ", amount=" + amount + ", date=" + date + "]";
    }
}
