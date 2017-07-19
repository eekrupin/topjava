package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MemoryDaoImpl;
import ru.javawebinar.topjava.dao.ObjectDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;


public class MealServlet extends HttpServlet{

    private static final Logger log = getLogger(MealServlet.class);

    private static ObjectDao dao = new MemoryDaoImpl();

    private final String LIST_MEAL = "/meals.jsp";
    private final String INSERT_OR_EDIT = "/meals.jsp";

    @Override
    public void init() throws ServletException {
        super.init();
        MealsUtil.getFakeMeals().forEach( meal -> dao.add(meal));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meal");

        String action = request.getParameter("action");
        action = action == null ? "" : action;

        if (action.equalsIgnoreCase("delete")){
            int mealId = Integer.parseInt( request.getParameter("mealId") );
            log.debug("delete meal: " + mealId);
            dao.delete(mealId);
            response.sendRedirect("meals");
        }
        else if (action.equalsIgnoreCase ("edit")){
            int mealId = Integer.parseInt( request.getParameter("mealId") );
            log.debug("edit meal: " + mealId);
            Meal meal = (Meal)dao.getObjectDB(mealId);
            request.setAttribute("meal", meal);
            request.setAttribute("meals", MealsUtil.getMealsWithExceed( dao.getObjectsDB() ) );
            request.getRequestDispatcher(INSERT_OR_EDIT).forward(request, response);
        }
        else  {
            request.setAttribute("meals", MealsUtil.getMealsWithExceed( dao.getObjectsDB() ) );
            request.getRequestDispatcher(LIST_MEAL).forward(request, response);
        }

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
            dao.update(meal);
        }
        else {
            log.debug("add meal");
            dao.add(meal);
        }

        response.sendRedirect("meals");
    }

}
