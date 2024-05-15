package com.management_system.ultilities.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Document("account")
@Builder
public class Account implements UserDetails, Serializable {
    @Setter
    @Field(name = "user_name")
    @JsonProperty("user_name")
    @Indexed(unique = true)
    String userName;

    @Setter
    @Field(name = "password")
    String password;

    @Getter
    @Setter
    @Field(name = "role")
    String role;

    @Getter
    @Setter
    @JsonProperty("current_jwt")
    @Field(name = "current_jwt")
    String currentJwt;

    @Getter
    @Setter
    @JsonProperty("current_fcm_token")
    @Field(name = "current_fcm_token")
    String currentFcmToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
