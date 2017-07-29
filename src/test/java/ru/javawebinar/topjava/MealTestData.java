package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.BeanMatcher;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MealTestData {

    public static Integer sequence = ru.javawebinar.topjava.model.BaseEntity.START_SEQ + 2;

    //В MealTestData еду делайте константами. Не надо Map конструкций!
    //Прочитал уже после того как сделал. На "погоду" вроде не влияет, зачем константы - не понял.
    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(sequence++,LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510),
            new Meal(sequence++,LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
            new Meal(sequence++,LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
            new Meal(sequence++,LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
            new Meal(sequence++, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
            new Meal(sequence++, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500)
    );

    public static final BeanMatcher<Meal> MATCHER = new BeanMatcher<>(
            (expected, actual) -> expected == actual || expected.toString().equals(actual.toString())
    );

}
