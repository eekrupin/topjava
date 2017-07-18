package ru.javawebinar.topjava.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryDaoImpl implements ObjectDao {

    private AtomicInteger counter = new AtomicInteger(0);
    private Map<Integer, ObjectDB> db = new ConcurrentSkipListMap<>();

    @Override
    public void add(ObjectDB objectDB) {
        objectDB.setId( counter.addAndGet(1) );
        db.put(objectDB.getId(), objectDB);
    }

    @Override
    public void update(ObjectDB objectDB) {
        db.put(objectDB.getId(), objectDB);
    }

    @Override
    public void delete(int id) {
        db.remove(id);
    }

    @Override
    public ObjectDB getObjectDB(int id) {
        return db.get(id);
    }

    public List<ObjectDB> getObjectsDB() {
        List<ObjectDB> objectDBS = new ArrayList<>();
        for (Map.Entry<Integer, ObjectDB> pair : db.entrySet()){
            objectDBS.add( pair.getValue() );
        }
        return objectDBS;

    }

}
