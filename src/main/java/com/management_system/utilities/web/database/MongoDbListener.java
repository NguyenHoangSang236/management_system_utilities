package com.management_system.utilities.web.database;

import com.management_system.utilities.entities.database.MongoDbEntity;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MongoDbListener extends AbstractMongoEventListener<MongoDbEntity> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<MongoDbEntity> event) {
        updateMongoDbEntity(event.getSource());
    }


    private void updateMongoDbEntity(MongoDbEntity entity) {
        entity.setLastUpdateDate(new Date());

        String userName = "Anonymous";
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (auth != null ) {
            userName = auth.getPrincipal().toString();
        }

        entity.setUpdatedUserName(userName);
    }
}
