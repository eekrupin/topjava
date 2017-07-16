package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public class MealServiceImpl implements MealService{

    private MealDao mealDao;

    public void setMealDao(MealDao mealDao) {
        this.mealDao = mealDao;
    }

    @Override
    public void add(Meal meal) {
        this.mealDao.add(meal);
    }

    @Override
    public void update(Meal meal) {
        this.mealDao.update(meal);
    }

    @Override
    public void delete(int id) {
        this.mealDao.delete(id);
    }

    @Override
    public Meal getMeal(int id) {
        return this.mealDao.getMeal(id);
    }

    @Override
    public List<Meal> getMeals() {
        return this.mealDao.getMeals();
    }
}
