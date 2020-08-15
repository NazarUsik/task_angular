package com.nixsolutions.usik.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {
    public static String getProperties(String url, String propName) throws IOException {
        File file = new File(url);

        Properties properties = new Properties();
        properties.load(new FileReader(file));

        return properties.getProperty(propName);
    }
}
