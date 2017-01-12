package com.kamabizbazti.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamabizbazti.security.entities.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "Purpose")
public class CustomPurpose extends GeneralPurpose {

    @ManyToOne()
    @JoinColumn(name = "id")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public CustomPurpose() {
        super();
    }

    public CustomPurpose(User user, String name) {
        super(name, PurposeType.CUSTOM);
        this.user = user;
        super.setuId(user.getId());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        super.setuId(user.getId());
    }

    public void setName(String name) {
        super.setName(name);
        super.setType(PurposeType.CUSTOM);
    }

    @Override
    public String toString() {
        return "CustomPurpose [user=" + user + "]";
    }

}
