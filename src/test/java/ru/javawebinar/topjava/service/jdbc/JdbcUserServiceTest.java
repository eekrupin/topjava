package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

/**
 * Created by eekrupin on 13.08.2017.
 */
@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest{
}
