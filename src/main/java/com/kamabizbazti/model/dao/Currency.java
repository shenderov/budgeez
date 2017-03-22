package com.kamabizbazti.model.dao;

import javax.persistence.*;

@SuppressWarnings({"UnusedDeclaration"})
@Entity
@Table(name = "Currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "general_seq_gen")
    @SequenceGenerator(name = "general_seq_gen", sequenceName = "G_ID_SEQ")
    private long id;

    @Column(name = "currencyCode", nullable = false, unique = true)
    private String currencyCode;

    @Column(name = "currencyName", nullable = false)
    private String currencyName;

    @Column(name = "currencySymbol")
    private Character currencySymbol;

    public Currency() {
        super();
    }

    public Currency(String currencyCode, String currencyName) {
        super();
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
    }

    public Currency(String currencyCode, String currencyName, Character currencySymbol) {
        super();
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
    }

    public long getId() {
        return id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Character getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(Character currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", currencyCode='" + currencyCode + '\'' +
                ", currencyName='" + currencyName + '\'' +
                ", currencySymbol=" + currencySymbol +
                '}';
    }
}
