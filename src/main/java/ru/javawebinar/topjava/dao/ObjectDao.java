package ru.javawebinar.topjava.dao;

import java.util.List;

public interface ObjectDao {

    void add(ObjectDB objectDB);
    void update(ObjectDB objectDB);
    void delete(int id);
    ObjectDB getObjectDB(int id);
    List<ObjectDB> getObjectsDB();

}
