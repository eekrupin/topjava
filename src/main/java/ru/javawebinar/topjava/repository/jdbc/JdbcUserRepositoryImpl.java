package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);
            deleteRoles(user);
        }
        insertRoles(user);
        return user;
    }

    private void deleteRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
    }

    private void insertRoles(User user) {
        Set<Role> roles = user.getRoles();
        if (roles.size()==0) {
            return;
        }
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", roles, roles.size(),
                (ps, argument) -> {ps.setInt(1, user.getId());
                                   ps.setString(2, argument.name()); } );
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if (user!=null) {
            user.setRoles(getUserRoles(user.getId()));
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        if (user!=null) {
            user.setRoles(getUserRoles(user.getId()));
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u ORDER BY name, email", ROW_MAPPER);
        HashMap<Integer, HashSet<Role>> usersRoles = getAllUsersRoles();
        users.forEach( user -> user.setRoles( usersRoles.getOrDefault( user.getId(), new HashSet<>() ) ) );
        return users;
    }

    private HashMap<Integer, HashSet<Role>> getAllUsersRoles() {
        List<Map<String, Object>> mapListUserRoles = jdbcTemplate.queryForList("SELECT ur.user_id as user_id, ur.role as role FROM user_roles ur");
        HashMap<Integer, HashSet<Role>> usersRoles = getUsersRolesContainsUserId(mapListUserRoles);
        return usersRoles;
    }

    private HashMap<Integer, HashSet<Role>> getUsersRolesContainsUserId(List<Map<String, Object>> mapListUserRoles) {
        HashMap<Integer, HashSet<Role>> usersRoles = new HashMap<>();
        for (Map<String, Object> mapUserRoles : mapListUserRoles) {
            int user_id = (int)mapUserRoles.get("user_id");
            String roleName = (String)mapUserRoles.get("role");
            HashSet<Role> userRoles = usersRoles.getOrDefault(user_id, new HashSet<Role>());
            userRoles.add(Role.valueOf(roleName));
            usersRoles.putIfAbsent(user_id, userRoles);
        }
        return usersRoles;
    }

    private HashSet<Role> getUserRoles(int id) {
        List<Map<String, Object>> mapListUserRoles = jdbcTemplate.queryForList("SELECT ur.user_id as user_id, ur.role as role FROM user_roles ur where user_id=?", id);
        HashSet<Role> userRoles = getUsersRolesContainsUserId(mapListUserRoles).get(id);
        return userRoles;
    }
}
