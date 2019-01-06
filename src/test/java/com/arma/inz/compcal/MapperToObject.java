package com.arma.inz.compcal;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class MapperToObject {
    public static Object JsonToObject(String json, Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            object = mapper.readValue(json, object.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static Object FileToObject(Object object) {
        File file = getFile(object.getClass().getSimpleName());
        ObjectMapper mapper = new ObjectMapper();
        return mapObject(object, file, mapper);
    }

    public static Object FileToObject(Object object, String filename) {
        File file = getFile(filename);
        ObjectMapper mapper = new ObjectMapper();
        return mapObject(object, file, mapper);
    }

    private static String getFolderPath() {
        return "/Users/arma/Downloads/compcal/src/test/resources/json/";
    }

    private static Object mapObject(Object object, File file, ObjectMapper mapper) {
        try {
            object = mapper.readValue(file, object.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    private static File getFile(String filename) {
        return new File(getFolderPath() + filename + ".json");
    }
}
