package com.budgeez.model.entities.external;

import com.budgeez.model.entities.dao.Currency;
import com.budgeez.model.entities.dao.Language;
import com.budgeez.model.entities.dao.User;
import com.budgeez.model.enumerations.UserStatus;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class EUserDetails {

    @NotBlank(message = "error.name.notblank")
    @Size(min = 2, max = 70, message = "error.name.size")
    private String name;
    private String email;
    private Language language;
    private Currency currency;

    @Min(value = 1, message = "error.numbers.dayofmonth")
    @Max(value = 31, message = "error.numbers.dayofmonth")
    private Integer startDay;
    private UserStatus status;

    public EUserDetails() {
        super();
    }

    public EUserDetails(User user) {
        this.name = user.getName();
        this.email = user.getUsername();
        this.language = user.getLanguage();
        this.currency = user.getCurrency();
        this.startDay = user.getStartDay();
        this.status = user.getStatus();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Integer getStartDay() {
        return startDay;
    }

    public void setStartDay(Integer startDay) {
        this.startDay = startDay;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "EUserDetails{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", language=" + language.toString() +
                ", currency=" + currency.toString() +
                ", startDay=" + startDay +
                ", status=" + status +
                '}';
    }
}
