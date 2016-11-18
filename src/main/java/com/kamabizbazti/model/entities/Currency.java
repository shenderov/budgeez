package com.kamabizbazti.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "Currency", uniqueConstraints = @UniqueConstraint(columnNames = {"currencyCode", "currencyName"}))
public class Currency {

    @Id
    @Column(name = "currencyCode", nullable = false)
    private String currencyCode;

    @Column(name = "currencyName", nullable = false)
    private String currencyName;

    public Currency() {
        super();
    }

    public Currency(String currencyCode, String currencyName) {
        super();
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setName(String currencyName) {
        this.currencyName = currencyName;
    }

    @Override
    public String toString() {
        return "Currency [currencyCode=" + currencyCode + ", currencyName=" + currencyName + "]";
    }
}
