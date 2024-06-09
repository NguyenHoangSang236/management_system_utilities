package com.management_system.utilities.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.utilities.core.filter.FilterOption;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest implements Serializable {
    @JsonProperty("filter_options")
    FilterOption  filterOption;

    Pagination pagination;
}
