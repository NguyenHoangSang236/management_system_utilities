package com.management_system.utilities.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.utilities.constant.ConstantValue;
import com.management_system.utilities.core.filter.FilterOption;
import com.management_system.utilities.entities.api.request.ApiRequest;
import com.management_system.utilities.entities.api.request.FilterRequest;
import com.management_system.utilities.entities.api.request.FilterSort;
import com.management_system.utilities.entities.api.request.Pagination;
import com.management_system.utilities.entities.database.MongoDbEntity;
import com.management_system.utilities.entities.exceptions.DataNotFoundException;
import com.management_system.utilities.entities.exceptions.IdNotFoundException;
import com.management_system.utilities.entities.exceptions.InvalidArgumentsException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class DbUtils {
    final MongoTemplate mongoTemplate;
    final ValueParsingUtils valueParsingUtils;


    /**
     * Use for document which DOES NOT NEED version control fields, id is not the default "_id" and is not in the request body
     *
     * @param idKey          the id key name
     * @param idVal          the id key value
     * @param fieldsToUpdate a Map of data we want to update
     * @param recordClass    record class we want to get from database, modify its fields and save back to the database
     */
    public void updateSpecificFields(String idKey, String idVal, Map<String, Object> fieldsToUpdate, Class<?> recordClass) {
        Object obj = mongoTemplate.findById(idVal, recordClass);

        if (obj == null) {
            throw new DataNotFoundException("Data with " + idKey + ": " + idVal + " not found");
        }

        Query query = new Query(Criteria.where(idKey).is(idVal));
        Update update = new Update();

        // set all key and value from 'fieldsToUpdate' to 'update'
        for (String key : fieldsToUpdate.keySet()) {
            update.set(key, fieldsToUpdate.get(key));
        }

        updateVersionControlFields(update);

        mongoTemplate.updateFirst(query, update, recordClass);
    }


    /**
     * Use for document which DOES NOT NEED version control fields
     *
     * @param fieldsToUpdate a Map of data we want to update
     * @param recordClass    record class we want to get from database, modify its fields and save back to the database
     */
    public void updateSpecificFields(Map<String, Object> fieldsToUpdate, Class<?> recordClass) {
        if (fieldsToUpdate.get("id") == null) {
            throw new IdNotFoundException("Can not found id field");
        }

        String idVal = fieldsToUpdate.get("id").toString();
        Object obj = mongoTemplate.findById(idVal, recordClass);

        if (obj == null) {
            throw new DataNotFoundException("Data with " + "_id" + ": " + idVal + " not found");
        }

        Query query = new Query(Criteria.where("_id").is(idVal));
        Update update = new Update();

        // set all key and value from 'fieldsToUpdate' to 'update'
        for (String key : fieldsToUpdate.keySet()) {
            update.set(key, fieldsToUpdate.get(key));
        }

        updateVersionControlFields(update);

        mongoTemplate.updateFirst(query, update, recordClass);
    }


    /**
     * Filter data with pagination
     *
     * @param request     filter request
     * @param targetClass class type of results objects
     * @param <T>         class extending FilterOption
     * @param <U>         class type of result objects
     * @return a list of result objects after querying the database using data from request
     */
    @SneakyThrows
    public <T extends FilterOption, U extends MongoDbEntity> List<U> filterData(FilterRequest<T> request, Class<U> targetClass) {
        if (targetClass == null) {
            throw new InvalidArgumentsException("Target class must not be null");
        } else if (request == null) {
            return mongoTemplate.find(new Query(), targetClass);
        } else {
            Criteria criteria = new Criteria();
            Map<String, Object> optionMap = request.getFilterOption().toMap();
            Pagination pagination = request.getPagination();
            Sort sort = getSortFromRequestFilterSort(request.getSorts());

            for (String key : optionMap.keySet()) {
                Object value = optionMap.get(key);

                if (value != null) {
                    // search regex
                    if ((key.contains("name") || key.contains("email") || key.contains("address") || key.contains("phone_number")) &&
                            !value.toString().isBlank()) {
                        criteria.and(key).regex(".*" + value + ".*", "i");
                    }
                    // search exact elements in a field with type List
                    else if (value instanceof List) {
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

            Query query = new Query().addCriteria(criteria);

            if (pagination != null && pagination.getPage() > 0 && pagination.getSize() > 0) {
                query.with(PageRequest.of(pagination.getPage() - 1, pagination.getSize()));
            } else {
                query.with(PageRequest.of(0, ConstantValue.DEFAULT_PAGE_SIZE));
            }

            if (sort != null) {
                query.with(sort);
            }

            return mongoTemplate.find(query, targetClass);
        }
    }


    /**
     * Filter data to retrieve all
     *
     * @param request     filter request
     * @param targetClass class type of results objects
     * @param <U>         class type of result objects
     * @return a list of result objects after querying the database using data from request
     */
    @SneakyThrows
    public <U extends MongoDbEntity> List<U> filterData(ApiRequest request, Class<U> targetClass) {
        if (targetClass == null) {
            throw new InvalidArgumentsException("Target class must not be null");
        } else if (request == null) {
            return mongoTemplate.find(new Query(), targetClass);
        } else {
            Criteria criteria = new Criteria();
            Map<String, Object> optionMap = request.toMap();

            for (String key : optionMap.keySet()) {
                Object value = optionMap.get(key);

                if (value != null) {
                    // search regex
                    if ((key.contains("name") || key.contains("email") || key.contains("address") || key.contains("phone_number")) &&
                            !value.toString().isBlank()) {
                        criteria.and(key).regex(".*" + value + ".*", "i");
                    }
                    // search exact elements in a field with type List
                    else if (value instanceof List) {
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

            Query query = new Query().addCriteria(criteria);

            return mongoTemplate.find(query, targetClass);
        }
    }


    /**
     * Use for merging data from request to the MongoDbEntity and documents which need version management fields
     *
     * @param mongoEntity the entity we want to merge from a request
     * @param req         the ApiRequest we want to merge
     * @param <T>         class extending MongoDbEntity
     * @param <R>         class extending ApiRequest
     * @return a MongoDbEntity after merging data from request
     */
    public <T extends MongoDbEntity, R extends ApiRequest> T mergeMongoEntityFromRequest(T mongoEntity, R req) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            // convert request to Map
            Map<String, Object> reqMap = objectMapper.convertValue(req, Map.class);

            Class<? extends MongoDbEntity> mongoEntityClass = mongoEntity.getClass();

            for (Map.Entry<String, Object> entry : reqMap.entrySet()) {
                try {
                    Field field = mongoEntityClass.getDeclaredField(valueParsingUtils.fromSnakeCaseToCamel(entry.getKey()));

                    field.setAccessible(true);

                    Object value = entry.getValue();

                    if (field.getType().isEnum()) {
                        @SuppressWarnings("rawtypes")
                        Class<? extends Enum> enumType = (Class<? extends Enum>) field.getType();
                        value = Enum.valueOf(enumType, value.toString());
                    }

                    field.set(mongoEntity, value);
                } catch (NoSuchFieldException e) {
                    log.error(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mongoEntity;
    }


    /**
     * Use for merging data from request to an entity
     *
     * @param object  the object we want to merge from a request
     * @param request the request we want to merge
     * @param <T>     the class type you want to merge from request
     * @return object after merging from request
     */
    public <T> T mergeObjectFromRequest(T object, Object request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            Map<String, Object> reqMap = objectMapper.convertValue(request, Map.class);

            Class<T> entityClass = (Class<T>) object.getClass();

            for (Map.Entry<String, Object> entry : reqMap.entrySet()) {
                Field field = entityClass.getDeclaredField(valueParsingUtils.fromSnakeCaseToCamel(entry.getKey()));
                field.setAccessible(true);

                Object value = entry.getValue();

                if (field.getType().isEnum()) {
                    @SuppressWarnings("rawtypes")
                    Class<? extends Enum> enumType = (Class<? extends Enum>) field.getType();
                    value = Enum.valueOf(enumType, value.toString());
                }

                field.set(object, value);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }


    /**
     * Update specific fields of a record for versions control
     *
     * @param update includes fields and their values
     */
    private void updateVersionControlFields(Update update) {
        update.inc("version", 1);
        update.set("last_update_date", new Date());

        String userName = "Anonymous";
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            userName = auth.getPrincipal().toString();
        }

        update.set("updated_user_name", userName);
    }


    /**
     * Get Sort from request's FilterSort list
     *
     * @param sorts list of FilterSort
     * @return Sort data from the given FilterSort list
     */
    private Sort getSortFromRequestFilterSort(List<FilterSort> sorts) {
        if (sorts != null && !sorts.isEmpty()) {
            List<Sort.Order> orderList = new ArrayList<>();

            sorts.stream().forEach(
                    filterSort -> orderList.add(new Sort.Order(filterSort.getType(), filterSort.getKey()))
            );

            return Sort.by(orderList);
        } else {
            return null;
        }
    }
}
