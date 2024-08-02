package com.management_system.utilities.core.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.management_system.utilities.core.redis.RedisData;

import java.io.IOException;

public class RedisDataDeserializer extends StdDeserializer<RedisData> {
    Class<? extends RedisData> targetClass;

    public RedisDataDeserializer(Class<? extends RedisData> vc) {
        super(vc);
        this.targetClass = vc;
    }

    public RedisDataDeserializer(JavaType valueType) {
        super(valueType);
    }

    public RedisDataDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    public RedisData deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        return mapper.treeToValue(node, this.targetClass);
    }
}