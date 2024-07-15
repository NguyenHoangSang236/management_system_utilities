package com.management_system.utilities.utils;

import com.management_system.utilities.core.filter.FilterOption;
import com.management_system.utilities.entities.Pagination;
import com.management_system.utilities.entities.exceptions.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DbUtils {
    @Autowired
    private MongoTemplate mongoTemplate;


    public void updateSpecificFields(String idKey, String idVal, Map<String, Object> fieldsToUpdate, Class<?> recordClass) {
        Object obj = mongoTemplate.findById(idVal, recordClass);

        if(obj == null) {
            throw new DataNotFoundException("Data with " + idKey + ": " + idVal + " not found");
        }

        Query query = new Query(Criteria.where(idKey).is(idVal));
        Update update = new Update();

        // set all key and value from 'fieldsToUpdate' to 'update'
        for(String key: fieldsToUpdate.keySet()) {
            update.set(key, fieldsToUpdate.get(key));
        }

        mongoTemplate.updateFirst(query, update, recordClass);
    }


    public <T extends FilterOption, U> List<U> filterData(T filterOptions, Pagination pagination, Class<U> tartgetClass) {
        Criteria criteria = new Criteria();
        Map<String, Object> optionMap = filterOptions.toMap();

        for(String key: optionMap.keySet()) {
            Object value = optionMap.get(key);

            if(value != null) {
                if(key.contains("name") && !value.toString().isBlank()) {
                    criteria.and(key).regex(".*" + value + ".*", "i");
                }
                else {
                    criteria.and(key).is(value);
                }
            }
        }

        Query query = new Query()
                .addCriteria(criteria)
                .with(PageRequest.of(pagination.getPage() - 1, pagination.getSize()));

        return mongoTemplate.find(query, tartgetClass);
    }


    public <T> T getDataById(String id, Class<T> tartgetClass) {
        Criteria criteria = new Criteria();
        criteria.and("_id").is(id);
        Query query = new Query().addCriteria(criteria);

        List<T> resList = mongoTemplate.find(query, tartgetClass);

        return resList.isEmpty() ? null : resList.get(0);
    }
}
