package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import java.util.Arrays;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest{

    @Test
    public void testGetWithMeals() throws Exception {
        User user = service.getWithMeals(ADMIN_ID);
        UserTestData.MATCHER.assertEquals(UserTestData.ADMIN, user);
        MealTestData.MATCHER.assertCollectionEquals( Arrays.asList(MealTestData.ADMIN_MEAL2, MealTestData.ADMIN_MEAL1) , user.getMeals());
    }

}
