package example.HelperClassTUT;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {


    public ConfigReader() {
    }


    public static  final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
