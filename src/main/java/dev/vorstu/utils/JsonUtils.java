package dev.vorstu.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    public static String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
