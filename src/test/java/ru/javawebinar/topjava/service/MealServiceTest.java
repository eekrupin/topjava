package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.MATCHER;
import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testSave() throws Exception {
        Meal newMeal =  new Meal(LocalDateTime.of(2016, Month.MAY, 30, 12, 0), "Ланч", 500);
        Meal created = service.save(newMeal, USER_ID);
        newMeal.setId(created.getId());

        ArrayList<Meal> testMeals = new ArrayList<>(MealTestData.MEALS);
        testMeals.add(0, newMeal);
        MATCHER.assertCollectionEquals(testMeals, service.getAll(USER_ID));
    }

    @Test
    public void testDelete() throws Exception {
        ArrayList<Meal> testMeals = new ArrayList<>(MealTestData.MEALS);
        Integer id = testMeals.get(0).getId();
        testMeals.remove(0);
        service.delete(id, USER_ID);
        MATCHER.assertCollectionEquals(testMeals, service.getAll(USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundDelete(){
        service.delete(1, 1);
    }

    @Test
    public void testGet() throws Exception {
        Meal meal = service.get(MEALS.get(0).getId(), USER_ID);
        MATCHER.assertEquals(MEALS.get(0), meal);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        service.get(1, 1);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Meal> meals = service.getAll(USER_ID);
        MATCHER.assertCollectionEquals(MEALS, meals);
    }

    @Test
    public void testUpdate() throws Exception {
        Integer id = MEALS.get(0).getId();
        Meal updated = service.get(id, USER_ID);
        updated.setDescription("Бизнес ланч");
        updated.setCalories(650);
        service.update(updated, USER_ID);
        MATCHER.assertEquals(updated, service.get(id, USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNotFound() throws Exception {
        Integer id = MEALS.get(0).getId();
        Meal updated = service.get(id, 1);
        updated.setDescription("Бизнес ланч");
        updated.setCalories(650);
        service.update(updated, USER_ID);
    }

    @Test
    public void getBetweenDates() throws Exception {
        LocalDate start = LocalDate.of(2015, 5, 31);
        LocalDate end = LocalDate.of(2015, 5, 31);
        List<Meal> betweenDates = service.getBetweenDates(start, end, USER_ID);
        MATCHER.assertCollectionEquals( Arrays.asList(MEALS.get(0), MEALS.get(1), MEALS.get(2)), betweenDates);
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
        LocalDateTime start = LocalDateTime.of( LocalDate.of(2015, 5, 30), LocalTime.of(13,0) );
        LocalDateTime end = LocalDateTime.of( LocalDate.of(2015, 5, 31), LocalTime.of(13,0) );
        List<Meal> betweenDateTimes = service.getBetweenDateTimes(start, end, USER_ID);
        MATCHER.assertCollectionEquals( Arrays.asList(
                MEALS.get(1),
                MEALS.get(2),
                MEALS.get(3),
                MEALS.get(4)
        ), betweenDateTimes);
    }
}