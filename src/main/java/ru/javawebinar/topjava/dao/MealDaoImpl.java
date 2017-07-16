package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.FakeMeals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoImpl implements MealDao {

    private static AtomicInteger counter = new AtomicInteger(0);
    private static Map<Integer, Meal> meals = new ConcurrentSkipListMap<>();
    {
        setFakeMeals();
    }

    @Override
    public void add(Meal meal) {
        meal.setId( counter.addAndGet(1) );
        meals.put(meal.getId(), meal);
    }

    @Override
    public void update(Meal meal) {
        meals.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }

    @Override
    public Meal getMeal(int id) {
        return meals.get(id);
    }

    public List<Meal> getMeals() {
        List<Meal> mealList = new ArrayList<>();
        for (Map.Entry<Integer, Meal> pair : meals.entrySet()){
            mealList.add( pair.getValue() );
        }
        return mealList;

    }

    private static void setFakeMeals() {
        List<Meal> fakeMeals = new FakeMeals().getMeals();
        fakeMeals.forEach( meal -> {meal.setId( counter.addAndGet(1) );
                                    meals.put(meal.getId(), meal );
                                    } );
    }

}
