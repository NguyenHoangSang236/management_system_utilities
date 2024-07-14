package com.management_system.utilities.core.redis;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.utilities.constant.enumuration.FilterType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RedisRequest {
    @JsonProperty(value = "type")
    @Enumerated(EnumType.STRING)
    FilterType type;

    Map<String, Object> data;
}
