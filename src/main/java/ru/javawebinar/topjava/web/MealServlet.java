package ru.javawebinar.topjava.web;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ConfigurableApplicationContext appCtx;
    private MealRestController controller;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //repository = new InMemoryMealRepositoryImpl();
        appCtx = new ClassPathXmlApplicationContext("/spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        controller.save(meal);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String command = request.getParameter("command");
        Boolean setFilter = command == null || !command.equals("clearFilter");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/meal.jsp").forward(request, response);
                break;
            case "all":
            default:
                List<MealWithExceed> meals;
                if (setFilter) {
                    Map<String, Object> params = getParamsFromRequest(request);
                    log.info("getFilteredByPeiod {}", params);
                    setRequestAttributes(request, params);
                    meals = MealsUtil.getWithExceeded(controller.getFilteredByPeiod(
                            (LocalDate)params.get("dateFrom"),
                            (LocalTime)params.get("timeFrom"),
                            (LocalDate)params.get("dateTo"),
                            (LocalTime)params.get("timeTo") ), MealsUtil.DEFAULT_CALORIES_PER_DAY);
                }
                else {
                    log.info("getAll");
                    meals = MealsUtil.getWithExceeded(controller.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY);
                }
                request.setAttribute("meals", meals);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private void setRequestAttributes(HttpServletRequest request, Map<String, Object> params) {
        request.setAttribute("dateFrom", params.get("dateFrom"));
        request.setAttribute("timeFrom", params.get("timeFrom"));

        request.setAttribute("dateTo", params.get("dateTo"));
        request.setAttribute("timeTo", params.get("timeTo"));
    }

    private Map<String, Object> getParamsFromRequest(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();

        String dateFrom = request.getParameter("dateFrom");
        String dateTo = request.getParameter("dateTo");
        String timeFrom = request.getParameter("timeFrom");
        String timeTo = request.getParameter("timeTo");

        if (dateFrom!=null && !dateFrom.isEmpty()) params.put("dateFrom", LocalDate.parse(dateFrom));
        if (dateTo!=null && !dateTo.isEmpty()) params.put("dateTo", LocalDate.parse(dateTo));
        if (timeFrom!=null && !timeFrom.isEmpty()) params.put("timeFrom", LocalTime.parse(timeFrom));
        if (timeTo!=null && !timeTo.isEmpty()) params.put("timeTo", LocalTime.parse(timeTo));

        return params;
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
