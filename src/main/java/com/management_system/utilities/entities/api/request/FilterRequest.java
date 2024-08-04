package com.management_system.utilities.entities.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.utilities.core.filter.FilterOption;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest implements Serializable {
    @JsonProperty("filter_options")
    FilterOption filterOption;

    Pagination pagination;

    List<FilterSort> sorts;
}
