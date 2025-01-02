package com.management_system.utilities.entities.database;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class MongoDbEntity extends DbEntity {
    @Version
    @Field(name = "version")
    Long version;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty(value = "last_update_date")
    @Field(name = "last_update_date")
    Date lastUpdateDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty(value = "creation_date")
    @Field(name = "creation_date")
    Date creationDate;

    @JsonProperty(value = "updated_user_name")
    @Field(name = "updated_user_name")
    String updatedUserName;
}
