package com.kamabizbazti.model.entities;

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
import com.kamabizbazti.security.entities.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @JoinColumn(name = "purposeId")
    private GeneralPurpose purpose;

    @Column(name = "purposeType")
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private PurposeType purposeType;

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

    public Record(User userId, GeneralPurpose purpose, Double amount, Long date) {
        super();
        this.userId = userId;
        this.purpose = purpose;
        this.purposeType = purpose.getType();
        this.amount = amount;
        this.date = date;
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

    public GeneralPurpose getPurpose() {
        return purpose;
    }

    public PurposeType getPurposeType() {
        return purposeType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPurpose(GeneralPurpose purpose) {
        this.purpose = purpose;
        this.purposeType = purpose.getType();
    }

    @Override
    public String toString() {
        return "Record [recordId=" + recordId + ", userId=" + userId + ", purpose=" + purpose + ", purposeType=" + purposeType
                + ", amount=" + amount + ", date=" + date + "]";
    }
}
