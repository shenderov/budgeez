package com.kamabizbazti.model.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamabizbazti.security.entities.Authority;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "User")
public class User {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "USER_SEQ", allocationSize = 1)
    @JsonIgnore
    private Long id;

    @Column(name = "username", length = 254, unique = true)
    @NotNull
    @Email
    //@Size(min = 4, max = 254)
    private String username;

    @Column(name = "password", length = 128)
    @NotNull
    @JsonIgnore
    @Size(min = 6, max = 128)
    private String password;

    @Column(name = "name", length = 70)
    @NotNull
    @Size(min = 2, max = 70)
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Authority> authorities;

    @ManyToOne(optional = false)
    @JoinColumn(name = "languageCode")
    @JsonIgnore
    private Language language;

    @ManyToOne(optional = false)
    @JoinColumn(name = "currencyCode")
    @JsonIgnore
    private Currency currency;

    @Range(min = 1, max = 31, message = "Start day must be in range between 1 and 31")
    @Column(name = "startDay", nullable = false)
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

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", enabled=" + enabled +
                ", lastPasswordResetDate=" + lastPasswordResetDate +
                ", authorities=" + authorities +
                ", language=" + language +
                ", currency=" + currency +
                ", startDay=" + startDay +
                ", isActivated=" + isActivated +
                ", creationDate=" + creationDate +
                '}';
    }
}