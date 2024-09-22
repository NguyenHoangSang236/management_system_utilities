package com.management_system.utilities.config.database;

import com.management_system.utilities.entities.database.MongoDbEntity;
import com.management_system.utilities.utils.SecurityUtils;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MongoDbListener extends AbstractMongoEventListener<MongoDbEntity> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<MongoDbEntity> event) {
        SecurityUtils.restoreSecurityContext();
        updateMongoDbEntity(event.getSource());
    }


    private void updateMongoDbEntity(MongoDbEntity entity) {
        Date currentDate = new Date();

        entity.setLastUpdateDate(currentDate);

        if(entity.getCreationDate() == null) {
            entity.setCreationDate(currentDate);
        }

        String userName = "Anonymous";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            userName = auth.getPrincipal().toString();
        }

        entity.setUpdatedUserName(userName);
    }
}
