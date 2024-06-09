package com.management_system.utilities.entities;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pagination implements Serializable {
    int page;
    int size;
}
