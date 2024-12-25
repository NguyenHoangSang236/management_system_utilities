package com.management_system.utilities.core.redis;

import com.management_system.utilities.entities.database.MongoDbEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class CachingProcessHandler {
    private MongoDbEntity dbEntity;
    private Context context;

    public CachingProcessHandler(MongoDbEntity dbEntity) {
        this.dbEntity = dbEntity;
    }

    public abstract void process();
}
