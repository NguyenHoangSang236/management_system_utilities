package com.management_system.utilities.core.redis;

import com.management_system.utilities.entities.database.MongoDbEntity;
import com.management_system.utilities.entities.exceptions.DataNotFoundException;
import com.management_system.utilities.entities.exceptions.InvalidDataException;
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

    /**
     * Execute some logic for context within caching process
     *
     */
    public abstract void process();
}
