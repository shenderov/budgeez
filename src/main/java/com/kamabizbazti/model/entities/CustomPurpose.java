package com.kamabizbazti.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamabizbazti.security.entities.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Purpose")
public class CustomPurpose extends GeneralPurpose {

@ManyToOne()
@JoinColumn(name="id")
@JsonIgnore
private User user;

public CustomPurpose() {
	super();
}

public CustomPurpose(User user, String name) {
	super(name, PurposeType.CUSTOM);
	this.user = user;
}

public User getUser() {
	return user;
}

	public void setUser(User user) {
		this.user = user;
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
