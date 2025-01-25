package com.management_system.utilities.entities.api.request;

import java.util.Map;

public abstract class ApiRequest {
    public abstract Map<String, Object> toMap();
}
