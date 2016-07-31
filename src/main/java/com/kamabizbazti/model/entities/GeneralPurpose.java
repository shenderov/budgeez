package com.kamabizbazti.model.entities;

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

@Entity
@Table(name = "Purpose", uniqueConstraints = @UniqueConstraint(columnNames = {"purposeId"}))
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class GeneralPurpose {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="purpose_seq_gen")
	@SequenceGenerator(name="purpose_seq_gen", sequenceName="PURPOSE_SEQ")
	@Column(name = "purposeId", nullable = false)
	private int purposeId;
	
	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private PurposeType type;

	@Column(name = "name", nullable = false)
	private String name;

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

	public int getPurposeId() {
		return purposeId;
	}

	public PurposeType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "GeneralPurpose [purposeId=" + purposeId + ", type=" + type + ", name=" + name + "]";
	}
}
