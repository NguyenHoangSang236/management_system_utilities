package com.management_system.utilities.entities.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.utilities.core.filter.FilterOption;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest<T extends FilterOption> extends ApiRequest implements Serializable {
    @JsonProperty("filter_options")
    T filterOption;

    Pagination pagination;

    List<FilterSort> sorts;

    @Override
    public Map<String, Object> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, new TypeReference<>() {});
    }
}
