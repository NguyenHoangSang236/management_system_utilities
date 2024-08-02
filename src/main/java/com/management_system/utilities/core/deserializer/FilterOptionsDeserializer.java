package com.management_system.utilities.core.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.management_system.utilities.core.filter.FilterOption;

import java.io.IOException;

public class FilterOptionsDeserializer extends StdDeserializer<FilterOption> {
    Class<? extends FilterOption> targetClass;

    public FilterOptionsDeserializer(Class<? extends FilterOption> vc) {
        super(vc);
        this.targetClass = vc;
    }

    public FilterOptionsDeserializer(JavaType valueType) {
        super(valueType);
    }

    public FilterOptionsDeserializer(StdDeserializer<?> src) {
        super(src);
    }


    @Override
    public FilterOption deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        final ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();

        return mapper.treeToValue(node, this.targetClass);
    }
}
