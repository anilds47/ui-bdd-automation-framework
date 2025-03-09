package com.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {

    public static Properties loadProperty() {
        File file = new File(DirectoryPaths.configPath);
        Properties prop = new Properties();
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
            prop.load(fileInput);
        } catch (Exception e) {
           // LogUtil.errorLog(ConfigReaderWriter.class, "Caught the exception", e);
            System.out.println("Caught exception"+e);
        }
        return prop;

    }

    public static String getValue(String key) {
        String value = null;
        Properties prop;
        try {
            prop = loadProperty();
            value = prop.getProperty(key);
           // LogUtil.infoLog(ConfigReaderWriter.class, "Key is: " + key + " , Value is: " + valueFromConfigReader);
            System.out.println("Key is: " + key + "value is: " + value);
        } catch (Exception e) {
           // LogUtil.errorLog(ConfigReaderWriter.class, e.getMessage());
            e.printStackTrace();
        }
        return value;
    }


}
