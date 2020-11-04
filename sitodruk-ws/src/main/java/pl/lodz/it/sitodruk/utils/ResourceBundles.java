package pl.lodz.it.sitodruk.utils;

import lombok.extern.java.Log;
import pl.lodz.it.sitodruk.exceptions.BaseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log
public class ResourceBundles {


    public static Properties loadProperties(String propertiesKey) throws BaseException {
        InputStream inputStream = ResourceBundles.class.getClassLoader().getResourceAsStream(propertiesKey);
        Properties properties = new Properties();
        try {
            if(inputStream != null)
                properties.load(inputStream);
        } catch (IOException e) {
            log.severe("Error occurred while loading properties file");
        }
        return properties;
    }
}