package com.kamabizbazti.security.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamabizbazti.model.entities.Currency;
import com.kamabizbazti.model.entities.Language;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "User", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "USER_SEQ", allocationSize = 1)
    @JsonIgnore
    private Long id;

    @Column(name = "username", length = 50, unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    private String username;

    @Column(name = "password", length = 100)
    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    @Column(name = "name", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String name;

    @Column(name = "enabled")
    @NotNull
    @JsonIgnore
    private Boolean enabled;

    @Column(name = "lastPasswordResetDate")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @JsonIgnore
    private Date lastPasswordResetDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
    @JsonIgnore
    private List<Authority> authorities;

    @ManyToOne(optional = false)
    @JoinColumn(name="languageCode")
    @JsonIgnore
    private Language language;

    @ManyToOne(optional = false)
    @JoinColumn(name="currencyCode")
    @JsonIgnore
    private Currency currency;

    @Range(min=1, max=31, message="Start day must be in range between 1 and 31")
    @Column(name = "startCay", nullable = false)
    @JsonIgnore
    private int startDay;

    @Column(name = "isActivated", nullable = false)
    @JsonIgnore
    private boolean isActivated;

    @Column(name = "creationDate", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @JsonIgnore
    private Date creationDate;

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.enabled = true;
        this.lastPasswordResetDate = new Date();
        this.startDay = 1;
        this.isActivated = false;
        this.creationDate = new Date();
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public Language getLanguage() {
        return language;
    }

    public Currency getCurrency() {
        return currency;
    }

    public int getStartDay() {
        return startDay;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }
}