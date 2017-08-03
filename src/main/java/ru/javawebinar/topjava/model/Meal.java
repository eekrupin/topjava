package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM meals WHERE id=:id AND user_id=:user_id"),
        @NamedQuery(name = Meal.DELETE_ALL, query = "DELETE FROM meals WHERE user_id=:user_id"),
        @NamedQuery(name = Meal.GET, query = "SELECT * FROM meals WHERE id = :id AND user_id = :user_id"),
        @NamedQuery(name = Meal.GET_ALL, query = "SELECT * FROM meals WHERE user_id=:user_id ORDER BY date_time DESC"),
        @NamedQuery(name = Meal.GET_BETWEEN, query = "SELECT * FROM meals WHERE user_id=:user_id  AND date_time BETWEEN :startDate AND :endDate ORDER BY date_time DESC")
})

@Entity
@Table(name = "meals", uniqueConstraints = {@UniqueConstraint(columnNames = "user_id, dateTime", name = "meals_unique_user_datetime_idx")})
public class Meal extends BaseEntity {

    public static final String DELETE = "meal.delete";
    public static final String DELETE_ALL = "meal.deleteAll";
    public static final String GET = "meal.get";
    public static final String GET_ALL = "meal.getAll";
    public static final String GET_BETWEEN = "meal.getBetween";

    @Column(name = "dateTime", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @NotEmpty
    private String description;

    @Column(name = "calories", nullable = false)
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
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

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
