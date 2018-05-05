package com.budgeez.security.entities;

import com.budgeez.model.entities.dao.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    @Column(unique = true)
    private Token token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, unique = true, name = "user_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User user;

    private Long timestamp;

    private Date expirationDate;

    public PasswordResetToken() {
    }

    public PasswordResetToken(Token token, User user, Date expirationDate) {
        this.token = token;
        this.user = user;
        this.timestamp = System.currentTimeMillis();
        this.expirationDate = expirationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "PasswordResetToken{" +
                "id=" + id +
                ", token=" + token +
                ", user=" + user +
                ", timestamp=" + timestamp +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
