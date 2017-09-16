package ru.javawebinar.topjava.to;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class MealTo extends BaseTo implements Serializable{
    private static final long serialVersionUID = 5L;

    @NotNull
    private LocalDateTime dateTime;

    @NotBlank
    private String description;

    @Range(min = 10, max = 5000, message = "calories must between 10 and 5000")
    @NotNull
    private Integer calories;

    public MealTo() {
    }

    public MealTo(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + getId() +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }


}
