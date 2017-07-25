package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public Meal save(Meal meal) {
        log.info("save {} user {}", meal, AuthorizedUser.id());
        return service.save(meal, AuthorizedUser.id());
    }

    public Meal update(Meal meal) {
        log.info("update {} user {}", meal, AuthorizedUser.id());
        return save(meal);
    }

    public void delete(int id) {
        log.info("delete {} user {}", id, AuthorizedUser.id());
        service.delete(id, AuthorizedUser.id());
    }

    public Meal get(int id) {
        log.info("get {} user {}", id, AuthorizedUser.id());
        return service.get(id, AuthorizedUser.id());
    }

    public Collection<Meal> getAll() {
        log.info("getAll user {}", AuthorizedUser.id());
        return service.getAll(AuthorizedUser.id());
    }

    public Collection<Meal> getFilteredByPeriod(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getFilteredByPeriod startDate {}, startTime {}, endDate {}, endDate {}, user {}", startDate, startTime, endDate, endTime, AuthorizedUser.id());
        return service.getFilteredByPeriod(startDate, startTime, endDate, endTime, AuthorizedUser.id());
    }

}