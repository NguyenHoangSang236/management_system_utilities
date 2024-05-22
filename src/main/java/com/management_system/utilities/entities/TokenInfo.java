package com.management_system.utilities.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document("refresh_token")
@Builder
public class TokenInfo {
    @Id
    @Field(name = "id")
    String id;

    @Field(name = "token")
    @Indexed(unique = true)
    String token;

    @JsonProperty("user_name")
    @Field(name = "user_name")
    String userName;

    @Field(name = "roles")
    List<String> roles;

    @JsonIgnore
    public List<GrantedAuthority> getAuthorities() {
        if (roles == null) return new ArrayList<>();
        return roles.stream().map(s -> (GrantedAuthority) () -> s).toList();
    }
}
