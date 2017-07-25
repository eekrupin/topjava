package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository repository;

    @Override
    public Meal save(Meal meal, int userId) {
        Integer id = meal.getId();
        meal = repository.save(meal, userId);
        checkNotFound(meal!=null, "wrong meal by id: " + id);
        return meal;
    }

    @Override
    public Meal update(Meal meal, int userId) {
        return save(meal, userId);
    }

    @Override
    public void delete(int id, int userId) {
        boolean res = repository.delete(id, userId);
        checkNotFound(res, "wrong meal by id: " + id);
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id, userId);
        checkNotFound(meal!=null, "wrong meal by id: " + id);
        return meal;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Override
    public Collection<Meal> getFilteredByPeriod(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, int userId) {
        return repository.getFilteredByPeriod(startDate, startTime, endDate, endTime, userId);
    }
}