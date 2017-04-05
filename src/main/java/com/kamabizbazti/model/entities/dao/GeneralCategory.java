package com.kamabizbazti.model.entities.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamabizbazti.model.enumerations.CategoryType;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Table(name = "Category", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "uId", "type"}))
@Inheritance(strategy = SINGLE_TABLE)
public class GeneralCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "category_seq_gen")
    @SequenceGenerator(name = "category_seq_gen", sequenceName = "CATEGORY_SEQ")
    @Column(name = "categoryId", nullable = false, unique = true)
    private long categoryId;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @Column(name = "name")
    @NotBlank
    @Size(max = 30, min = 1)
    private String name;

    @Column(name = "uId")
    @JsonIgnore
    private long uId;

    public GeneralCategory() {
        super();
    }

    public GeneralCategory(String name) {
        super();
        this.type = CategoryType.GENERAL;
        this.name = name;
    }

    public GeneralCategory(String name, CategoryType type) {
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

    public long getCategoryId() {
        return categoryId;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
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
        return "GeneralCategory [categoryId=" + categoryId + ", type=" + type + ", name=" + name + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneralCategory category = (GeneralCategory) o;

        if (categoryId != category.categoryId) return false;
        if (uId != category.uId) return false;
        if (type != category.type) return false;
        return name != null ? name.equals(category.name) : category.name == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (categoryId ^ (categoryId >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (uId ^ (uId >>> 32));
        return result;
    }
}
