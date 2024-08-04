package com.management_system.utilities.repository;

import com.management_system.utilities.entities.database.TokenInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends MongoRepository<TokenInfo, String> {
    @Query("{'token': ?0}")
    TokenInfo getRefreshTokenInfoByToken(String token);

    @Query("{'user_name': ?0}")
    TokenInfo getRefreshTokenInfoByUserName(String userName);
}
