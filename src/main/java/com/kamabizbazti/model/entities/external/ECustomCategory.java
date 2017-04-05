package com.kamabizbazti.model.entities.external;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class ECustomCategory {

    @NotBlank(message = "error.customcategory.name.notblank")
    @Size(max = 30, min = 1, message = "error.customcategory.name.size")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
