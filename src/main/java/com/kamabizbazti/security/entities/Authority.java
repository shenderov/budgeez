package com.kamabizbazti.security.entities;

import com.kamabizbazti.model.entities.dao.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "Authority")
public class Authority implements Comparable<Authority> {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "authority_seq")
    @SequenceGenerator(name = "authority_seq", sequenceName = "AUTHORITY_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "name", length = 50, unique = true, updatable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthorityName name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authorities")
    private Set<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthorityName getName() {
        return name;
    }

    public void setName(AuthorityName name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }


    @Override
    public int compareTo(Authority authority) {
        if (authority.getId() > this.getId()) {
            return 1;
        } else {
            return -1;
        }
    }
}