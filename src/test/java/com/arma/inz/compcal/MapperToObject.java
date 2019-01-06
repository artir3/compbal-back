package com.arma.inz.compcal;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class MapperToObject {
    public static Object JsonToObject(String json, Object result) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.readValue(json, result.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object FileToObject(Object result, String folder) {
        File file = getFile(folder, result.getClass().getSimpleName());
        ObjectMapper mapper = new ObjectMapper();
        return mapObject(result, file, mapper);
    }

    public static Object FileToObject(Object result, String folder, String filename) {
        File file = getFile(folder, filename);
        ObjectMapper mapper = new ObjectMapper();
        return mapObject(result, file, mapper);
    }

    public static Object FileToObject(Collection<Object> result, String folder, String filename) {
        File file = getFile(folder, filename);
        ObjectMapper mapper = new ObjectMapper();
        return mapObject(result, file, mapper);
    }

    private static String getFolderPath(String folder) {
        return "/Users/arma/Downloads/compcal/src/test/resources/json/" + folder;
    }

    private static Object mapObject(Object result, File file, ObjectMapper mapper) {
        try {
            result = mapper.readValue(file, result.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static File getFile(String folder, String filename) {
        return new File(getFolderPath(folder) + filename + ".json");
    }
}
