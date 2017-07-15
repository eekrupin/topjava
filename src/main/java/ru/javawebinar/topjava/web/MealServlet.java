package ru.javawebinar.topjava.web;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.FakeMeals;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


public class MealServlet extends HttpServlet{

    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meal");

        List<Meal> meals = getMeals();
        List<MealWithExceed> mealsExceeds = getMealsWithExceed(meals);

        req.setAttribute("meals", mealsExceeds);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    private List<Meal> getMeals() {
        return new FakeMeals().getMeals();
    }

    private List<MealWithExceed> getMealsWithExceed(List<Meal> meals) {
        Boolean exceed = false;
        List<MealWithExceed> mealsWithExceeds = new ArrayList<>();
        for (Meal meal : meals) {
            exceed = exceed != exceed;
            mealsWithExceeds.add(MealsUtil.createWithExceed(meal, exceed));
        }
        return mealsWithExceeds;
    }
}
