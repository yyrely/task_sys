package com.chuncongcong.task.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hu
 * @date 2018/8/15 17:27
 */


public class JsonUtils {

    public static String toJSON(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.findAndRegisterModules();
        try {
            String json = mapper.writeValueAsString(o);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> getListObject(String json, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        JavaType jt = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
        List<T> list = null;
        try {
            list = mapper.readValue(json, jt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        T readValue = null;
        try {
            readValue = mapper.readValue(json, clazz);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readValue;
    }

    public static void main(String[] args) {

        Instant instant = Instant.now();
        String s = JsonUtils.toJSON(instant);
        System.out.println(s);

        System.out.println(JsonUtils.toObject(s, Instant.class));

    }
}
