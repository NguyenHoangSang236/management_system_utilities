package com.management_system.utilities.core.filter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public abstract class FilterOption {
    String type;

    public Map<String, Object> toMap() {return null;}
}
