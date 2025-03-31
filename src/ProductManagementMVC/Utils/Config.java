package ProductManagementMVC.Utils;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Properties properties = new Properties();
    static {
        try (FileInputStream fis = new FileInputStream("src/config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public static String get(String key) {
        return properties.getProperty(key);
    }
}
