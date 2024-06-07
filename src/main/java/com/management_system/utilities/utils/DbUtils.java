package com.management_system.utilities.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DbUtils {
    @Autowired
    private MongoTemplate mongoTemplate;


    public void updateSpecificFields(String idKey, String idVal, Map<String, Object> fieldsToUpdate, Class<?> recordClass) {
        Query query = new Query(Criteria.where(idKey).is(idVal));
        Update update = new Update();

        // set all key and value from 'fieldsToUpdate' to 'update'
        for(String key: fieldsToUpdate.keySet()) {
            update.set(key, fieldsToUpdate.get(key));
        }

        mongoTemplate.updateFirst(query, update, recordClass);
    }
}
