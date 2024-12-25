package com.jd.testdev.cyber.utils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

/**
 * Author Shawn Pan
 * Date  2024/12/9 18:01
 */
public class JsonMapper {
    private final ObjectMapper objectMapper;
    private static JsonMapper jsonMapper;

    private JsonMapper(){
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES,false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER,true);
    }

    public static JsonMapper sharedInstance(){
        if (jsonMapper == null){
            jsonMapper = new JsonMapper();
        }
        return jsonMapper;
    }

    public byte[] makeSerialize(Object object) throws JsonProcessingException {
        return this.objectMapper.writeValueAsBytes(object);
    }

    public <T>T makeDeserialize(String content, Class<T> valueType) throws IOException {
        return this.objectMapper.readValue(content,valueType);
    }
}
