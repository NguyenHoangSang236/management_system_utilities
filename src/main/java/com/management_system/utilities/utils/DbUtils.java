package com.management_system.utilities.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.utilities.core.filter.FilterOption;
import com.management_system.utilities.entities.Pagination;
import com.management_system.utilities.entities.database.MongoDbEntity;
import com.management_system.utilities.entities.exceptions.DataNotFoundException;
import com.management_system.utilities.entities.exceptions.IdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DbUtils {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ValueParsingUtils valueParsingUtils;

    /*
     for id is not default "_id" and is not in the request body
     use for document which DOES NOT NEED version management fields
     */
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

        updateMongoDbFields(update);

        mongoTemplate.updateFirst(query, update, recordClass);
    }


    /*
     for id is not default "_id" and is not in the request body
     use for document which DOES NOT NEED version management fields
     */
    public void updateSpecificFields(Map<String, Object> fieldsToUpdate, Class<?> recordClass) {
        if(fieldsToUpdate.get("id") == null) {
            throw new IdNotFoundException("Can not found id field");
        }

        String idVal = fieldsToUpdate.get("id").toString();
        Object obj = mongoTemplate.findById(idVal, recordClass);

        if(obj == null) {
            throw new DataNotFoundException("Data with " + "_id" + ": " + idVal + " not found");
        }

        Query query = new Query(Criteria.where("_id").is(idVal));
        Update update = new Update();

        // set all key and value from 'fieldsToUpdate' to 'update'
        for(String key: fieldsToUpdate.keySet()) {
            update.set(key, fieldsToUpdate.get(key));
        }

        updateMongoDbFields(update);

        mongoTemplate.updateFirst(query, update, recordClass);
    }


    /*
    filter data with pagination
     */
    public <T extends FilterOption, U> List<U> filterData(T filterOptions, Pagination pagination, Class<U> tartgetClass) {
        Criteria criteria = new Criteria();
        Map<String, Object> optionMap = filterOptions.toMap();

        for(String key: optionMap.keySet()) {
            Object value = optionMap.get(key);

            if(value != null) {
                // search regex
                if(key.contains("name") && !value.toString().isBlank()) {
                    criteria.and(key).regex(".*" + value + ".*", "i");
                }
                // search exactly elements in a field with type List
                else if(value instanceof List) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<String> list = objectMapper.convertValue(value, new TypeReference<>() {});
                    criteria.and(key).all(list);
                }
                // search exactly
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


    /*
    use for merging data from request to the mongoDB entity
    use for documents which need version management fields
     */
    public <T extends MongoDbEntity> T mergeMongoEntityFromRequest(T mongoEntity, Object req) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            Map<String, Object> reqMap = objectMapper.convertValue(req, Map.class);

            Class<? extends MongoDbEntity> mongoEntityClass = mongoEntity.getClass();

            for (Map.Entry<String, Object> entry : reqMap.entrySet()) {
                Field field = mongoEntityClass.getDeclaredField(valueParsingUtils.fromSnakeCaseToCamel(entry.getKey()));
                field.setAccessible(true);

                Object value = entry.getValue();

                if (field.getType().isEnum()) {
                    @SuppressWarnings("rawtypes")
                    Class<? extends Enum> enumType = (Class<? extends Enum>) field.getType();
                    value = Enum.valueOf(enumType, value.toString());
                }

                field.set(mongoEntity, value);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return mongoEntity;
    }


    private void updateMongoDbFields(Update update) {
        update.inc("version", 1);
        update.set("last_update_date", new Date());

        String userName = "Anonymous";
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (auth != null ) {
            userName = auth.getPrincipal().toString();
        }

        update.set("updated_user_name", userName);
    }
}
