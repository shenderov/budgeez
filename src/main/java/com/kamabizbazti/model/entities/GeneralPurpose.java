package com.kamabizbazti.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Purpose", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "uId", "type"}))

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class GeneralPurpose {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "purpose_seq_gen")
    @SequenceGenerator(name = "purpose_seq_gen", sequenceName = "PURPOSE_SEQ")
    @Column(name = "purposeId", nullable = false, unique = true)
    private long purposeId;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PurposeType type;

    @Column(name = "name")
    @NotNull
    @NotBlank
    @Size(max = 30, min = 1)
    private String name;

    @Column(name = "uId")
    @JsonIgnore
    private long uId;

    public GeneralPurpose() {
        super();
    }

    public GeneralPurpose(String name) {
        super();
        this.type = PurposeType.GENERAL;
        this.name = name;
    }

    public GeneralPurpose(String name, PurposeType type) {
        super();
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPurposeId() {
        return purposeId;
    }

    public PurposeType getType() {
        return type;
    }

    public void setType(PurposeType type) {
        this.type = type;
    }

    public long getuId() {
        return uId;
    }

    public void setuId(long uId) {
        this.uId = uId;
    }

    @Override
    public String toString() {
        return "GeneralPurpose [purposeId=" + purposeId + ", type=" + type + ", name=" + name + "]";
    }
}
