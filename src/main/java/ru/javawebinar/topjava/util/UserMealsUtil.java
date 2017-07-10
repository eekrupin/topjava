package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

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
        userMealWithExceeds.forEach(System.out::println);

        System.out.println();
        System.out.println("Second realization with loop");
        List<UserMealWithExceed> getFilteredWithExceeded_WithLoop = getFilteredWithExceededWithLoop(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
        getFilteredWithExceeded_WithLoop.forEach(System.out::println);

    }

    private static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> daysMap = mealList.stream()
                .collect( Collectors.toMap(
                        userMeal->userMeal.getDateTime().toLocalDate(),
                        UserMeal::getCalories,
                        Integer::sum ) );

        return mealList.stream()
                .filter( userMeal -> TimeUtil.isBetween( userMeal.getDateTime().toLocalTime(), startTime, endTime ) )
                .map(userMeal-> getUserMealWithExceed(userMeal, getExceed(daysMap, userMeal.getDateTime().toLocalDate(), caloriesPerDay)))
                .collect(Collectors.toList());
    }

    private static List<UserMealWithExceed>  getFilteredWithExceededWithLoop(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> daysMap = new HashMap<>();
        mealList.forEach(userMeal->daysMap.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum));

        List<UserMealWithExceed> list = new ArrayList<>();
        mealList.forEach(userMeal-> {
            Boolean isTheRightTime = TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime);
            if (isTheRightTime) {
                Boolean exceed = getExceed(daysMap, userMeal.getDateTime().toLocalDate(), caloriesPerDay);
                list.add(getUserMealWithExceed(userMeal, exceed));
            }
        } );

        return list;
    }

    private static UserMealWithExceed getUserMealWithExceed(UserMeal userMeal, Boolean exceed) {
        return new UserMealWithExceed(userMeal.getDateTime(),
                userMeal.getDescription(),
                userMeal.getCalories(),
                exceed);
    }

    private static Boolean getExceed(Map<LocalDate, Integer> daysMap, LocalDate date, int caloriesPerDay){
        return daysMap.getOrDefault(date, 0) > caloriesPerDay;
    }

}
