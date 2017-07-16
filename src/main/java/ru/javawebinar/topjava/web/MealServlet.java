package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImpl;
import ru.javawebinar.topjava.util.FakeMeals;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


public class MealServlet extends HttpServlet{

    private static final Logger log = getLogger(MealServlet.class);

    //Думал указать MealService, но в MealService не поставил setMealDao, т.к. думаю он должен быть именно в имплементации, а не в интерфейсе. Просто не знаю как правильно.
    private static MealServiceImpl mealService;

    private final String LIST_MEAL = "/meals.jsp";
    private final String INSERT_OR_EDIT = "/meals.jsp";

    {
        //Spring'a нет, соответственно не знаю как сделать иньекцию бина в класс.
        mealService = new MealServiceImpl();
        mealService.setMealDao(new MealDaoImpl());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meal");

        String forward = LIST_MEAL;
        String action = request.getParameter("action");
        action = action == null ? "" : action;

        Boolean needAttributeMeals;
        if (action.equalsIgnoreCase("delete")){
            log.debug("delete meal");
            int mealId = Integer.parseInt( request.getParameter("mealId") );
            mealService.delete(mealId);
            needAttributeMeals = true;
        }
        else if (action.equalsIgnoreCase ("edit")){
            log.debug("edit meal");
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt( request.getParameter("mealId") );
            Meal meal = mealService.getMeal(mealId);
            request.setAttribute("meal", meal);
            needAttributeMeals = true;
        }
        else {
            needAttributeMeals = true;
        }

        if (needAttributeMeals) request.setAttribute("meals", getMealsWithExceed( mealService.getMeals() ) );

        request.getRequestDispatcher(forward).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("post meal");
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));

        Meal meal = new Meal(dateTime, description, calories);
        Boolean haveId = !( id == null || id.isEmpty() );
        if (haveId) {
            log.debug("update meal");
            meal.setId(Integer.parseInt(id));
            mealService.update(meal);
        }
        else {
            log.debug("add meal");
            mealService.add(meal);
        }

        request.setAttribute("meals", getMealsWithExceed( mealService.getMeals() ) );
        request.getRequestDispatcher(LIST_MEAL).forward(request, response);
    }

    private List<Meal> getMeals() {
        return new FakeMeals().getMeals();
    }

    private List<MealWithExceed> getMealsWithExceed(List<Meal> meals) {
        return MealsUtil.getFilteredWithExceeded(meals, LocalTime.MIN, LocalTime.MAX, 2000);

    }

    //для иньекции бина.
    public static void setMealService(MealServiceImpl mealService) {
        MealServlet.mealService = mealService;
    }
}
