package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> userMealWithExceeds = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
        userMealWithExceeds.stream()
                .forEach(System.out::println);
    }

    private static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> daysMap = mealList.stream()
                .collect( Collectors.toMap(
                        userMeal->userMeal.getDateTime().toLocalDate(),
                        UserMeal::getCalories,
                        (cal1, cal2)-> cal1 + cal2 ) );

        List<UserMealWithExceed> list = new ArrayList<>();
        mealList.stream()
                .filter( userMeal -> TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime) &&
                        daysMap.get( userMeal.getDateTime().toLocalDate() ) > caloriesPerDay )
                .forEach( userMeal -> list.add( new UserMealWithExceed( userMeal.getDateTime(),
                                                                     userMeal.getDescription(),
                                                                     userMeal.getCalories(),
                                                                     true
                                                                     ) ) );
        return list;
    }
}
