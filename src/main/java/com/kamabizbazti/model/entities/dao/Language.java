package com.kamabizbazti.model.entities.dao;

import javax.persistence.*;

@SuppressWarnings({"UnusedDeclaration"})
@Entity
@Table(name = "Language")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "general_seq_gen")
    @SequenceGenerator(name = "general_seq_gen", sequenceName = "G_ID_SEQ")
    private long id;

    @Column(name = "languageCode", nullable = false, unique = true)
    private String languageCode;

    @Column(name = "languageName", nullable = false)
    private String languageName;

    @Column(name = "languageIcon")
    private String languageIcon;

    public Language() {
        super();
    }

    public Language(String languageCode, String languageName) {
        super();
        this.languageCode = languageCode;
        this.languageName = languageName;
    }

    public Language(String languageCode, String languageName, String languageIcon) {
        super();
        this.languageCode = languageCode;
        this.languageName = languageName;
        this.languageIcon = languageIcon;
    }

    public long getId() {
        return id;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageIcon() {
        return languageIcon;
    }

    public void setLanguageIcon(String languageIcon) {
        this.languageIcon = languageIcon;
    }

    @Override
    public String toString() {
        return "Language{" +
                "id=" + id +
                ", languageCode='" + languageCode + '\'' +
                ", languageName='" + languageName + '\'' +
                ", languageIcon='" + languageIcon + '\'' +
                '}';
    }
}
