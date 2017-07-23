package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

import static ru.javawebinar.topjava.repository.mock.InMemoryUserRepositoryImpl.PRE_DEFINE_ADMIN;
import static ru.javawebinar.topjava.repository.mock.InMemoryUserRepositoryImpl.PRE_DEFINE_USER;


@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    public static final Comparator<Meal> MEAL_COMPARATOR = Comparator.comparing(Meal::getDateTime);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach( meal -> save(meal, PRE_DEFINE_ADMIN) );
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
        }
        else meal = getMealWithUserId(meal, userId);

        if (meal != null) repository.put(meal.getId(), meal);
        return meal;
    }

    private Meal getMealWithUserId(Meal meal, Integer userId) {
        Meal mealDB = repository.get(meal.getId());
        checkNotFound(mealDB!=null, "Meal not found with id: " + meal.getId());
        if (mealDB.getUserId()!=userId) meal = null;
        else meal.setUserId(userId);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = repository.get(id);
        boolean result = false;
        if (isUsersMeal(meal, userId)) result = repository.remove(id) != null;
        return result;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        if (!isUsersMeal(meal, userId)) meal = null;
        return meal;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(MEAL_COMPARATOR.reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getFilteredByPeiod(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId &&
                        DateTimeUtil.isBetween(meal.getDateTime(), startDate, startTime, endDate, endTime) ) //перегруженный метод
                .sorted(MEAL_COMPARATOR.reversed())
                .collect(Collectors.toList());
    }

    private boolean isUsersMeal(Meal meal, int userId){
        return meal != null && meal.getUserId() == userId;
    }

}

