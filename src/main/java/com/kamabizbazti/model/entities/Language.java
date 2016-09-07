package com.kamabizbazti.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "Language", uniqueConstraints = @UniqueConstraint(columnNames = {"languageCode", "languageName"}))
public class Language {
	
@Id
@Column(name = "languageCode", nullable = false)
private String languageCode;

@Column(name = "languageName", nullable = false)
private String languageName;

public Language() {
	super();
}

public Language(String languageCode, String languageName) {
	super();
	this.languageCode = languageCode;
	this.languageName = languageName;
}

public String getLanguageCode() {
	return languageCode;
}

public String getLanguageName() {
	return languageName;
}

public void setName(String languageName) {
	this.languageName = languageName;
}

@Override
public String toString() {
	return "Language [languageCode=" + languageCode + ", languageName=" + languageName + "]";
}
}
