package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

public interface MealService {

    // null if wrong meal
    Meal save(Meal meal, int userId);

    // null if wrong meal
    Meal update(Meal meal, int userId);

    // false if not found
    boolean delete(int id, int userId);

    // null if not found
    Meal get(int id, int userId);

    Collection<Meal> getAll(int userId);

    Collection<Meal> getFilteredByPeiod(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, int userId);

}