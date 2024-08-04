package com.management_system.utilities.entities.api.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.domain.Sort;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterSort {
    @Enumerated(EnumType.STRING)
    Sort.Direction type;

    String key;
}
