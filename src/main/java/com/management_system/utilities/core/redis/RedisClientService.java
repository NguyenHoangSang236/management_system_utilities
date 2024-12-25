package com.management_system.utilities.core.redis;

import com.management_system.utilities.constant.enumuration.TableName;
import com.management_system.utilities.entities.database.MongoDbEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RedisClientService {
    MongoDbEntity getAndCacheDataFromOneTable(TableName tableName, String id, List<Class<? extends CachingProcessHandler>> cachingProcessHandlers) throws Exception;
}
