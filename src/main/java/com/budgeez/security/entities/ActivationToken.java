package com.budgeez.security.entities;

import com.budgeez.model.entities.dao.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "activation_token")
public class ActivationToken  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    private Token token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, unique = true, name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private Long timestamp;

    public ActivationToken() {
    }

    public ActivationToken(Token token, User user) {
        this.token = token;
        this.user = user;
        this.timestamp = System.currentTimeMillis();
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

    @Override
    public String toString() {
        return "ActivationToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", user=" + user.toString() +
                ", timestamp=" + timestamp +
                '}';
    }
}
