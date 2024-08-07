package com.management_system.utilities.repository;

import com.management_system.utilities.entities.database.TokenInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<TokenInfo, String> {
    @Query("{'token': ?0}")
    Optional<TokenInfo> getRefreshTokenInfoByToken(String token);

    @Query("{'user_name': ?0}")
    Optional<TokenInfo> getRefreshTokenInfoByUserName(String userName);

    @Query("{'user_name':?0, 'token': ?1}")
    Optional<TokenInfo> getRefreshTokenInfoByUserNameAndToken(String userName, String token);
}
