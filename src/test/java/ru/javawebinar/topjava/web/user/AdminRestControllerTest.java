package ru.javawebinar.topjava.web.user;

import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.UserUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.*;

public class AdminRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestController.REST_URL + '/';

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + ADMIN_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(ADMIN));
    }

    @Test
    public void testGetNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    public void testGetByEmail() throws Exception {
        mockMvc.perform(get(REST_URL + "by?email=" + ADMIN.getEmail())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(ADMIN));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + USER_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk());
        MATCHER.assertListEquals(Collections.singletonList(ADMIN), userService.getAll());
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(REST_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    public void testGetUnauth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetForbidden() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdate() throws Exception {
        String updated = "{\"name\":\"UpdatedName\",\"email\":\"user3@gmail.com\",\"password\":\"password\",\"enabled\":true,\"registered\":\"2017-09-20T09:12:40.953+0000\",\"roles\":[\"ROLE_USER\",\"ROLE_ADMIN\"],\"caloriesPerDay\":2300}";
        mockMvc.perform(put(REST_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(updated))
                .andExpect(status().isOk());

        User user = JsonUtil.readValue(updated, User.class);
        user.setId(USER_ID);
        MATCHER.assertEquals(user, userService.get(USER_ID));
    }

    @Test
    public void testCreate() throws Exception {
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JSON_NEW_USER_WITH_PASSWORD)).andExpect(status().isCreated());

        User returned = MATCHER.fromJsonAction(action);
        NEW_USER.setId(returned.getId());

        MATCHER.assertEquals(NEW_USER, returned);
        MATCHER.assertListEquals(Arrays.asList(ADMIN, NEW_USER, USER), userService.getAll());
    }

    @Test
    public void testGetAll() throws Exception {
        TestUtil.print(mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentListMatcher(ADMIN, USER)));
    }

    @Test
    public void testNotValidCreate() throws Exception {
        User user = new User(null, null, "newgmail.com", "newPass", 0, Role.ROLE_USER, Role.ROLE_ADMIN);
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(user))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                ;
    }

    @Test
    public void testNotValidUpdate() throws Exception {
        User updated = new User(USER);
        updated.setName(null);
        updated.setRoles(Collections.singletonList(Role.ROLE_ADMIN));
        mockMvc.perform(put(REST_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
        ;

    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testCreateDuplicateByEmail() throws Exception {
        //Пароль JsonUtil.writeValue срезает. Соответственно кормим json напрямую, иначе тоже 422, но по причине пароль пустой.
        String user = "{\"name\":\"New\",\"email\":\"user@yandex.ru\",\"password\":\"password\",\"enabled\":true,\"registered\":\"2017-09-20T09:12:40.953+0000\",\"roles\":[\"ROLE_USER\",\"ROLE_ADMIN\"],\"caloriesPerDay\":2300}";
        ResultActions actions = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(user));
        //беда с транзакциями в тесте
        actions.andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{'cause':'" + getCauseDuplicateEmail() + "'}"))
                ;

    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testUpdateDuplicateByEmail() throws Exception {
        String user = "{\"name\":\"New\",\"email\":\"admin@gmail.com\",\"password\":\"password\",\"enabled\":true,\"registered\":\"2017-09-20T09:12:40.953+0000\",\"roles\":[\"ROLE_USER\",\"ROLE_ADMIN\"],\"caloriesPerDay\":2300}";
        mockMvc.perform(put(REST_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(user))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{'cause':'" + getCauseDuplicateEmail() + "'}"))
        ;

    }
}