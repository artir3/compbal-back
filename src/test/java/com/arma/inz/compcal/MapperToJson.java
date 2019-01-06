package com.arma.inz.compcal;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;

@Log
public class MapperToJson {
    public static void convertToJson(Object object, String methodName) {
        File file = getFile(object);
        parseObjectAndSaveJsonToFile(object, file);
        log.info(methodName + "." + object.getClass().getName());
    }

    private static void parseObjectAndSaveJsonToFile(Object object, File file) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File getFile(Object object) {
        return new File(getFIlePath() + object.getClass().getSimpleName() + ".json");
    }

    private static String getFIlePath() {
        return "/Users/arma/Downloads/compcal/src/test/resources/objects/";
    }
}
