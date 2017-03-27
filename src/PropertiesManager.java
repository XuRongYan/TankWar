import java.io.IOException;
import java.util.Properties;

/**
 * 配置文件加载管理类
 * Created by 徐溶延 on 2017/3/27.
 */
public class PropertiesManager {
    private static Properties properties = new Properties();

    static {
        try {
            properties.load(PropertiesManager.class.getResourceAsStream("config/tank.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PropertiesManager() {
    }

    public static String getStrProperties(String key) {
        return properties.getProperty(key);
    }

    public static int getIntProperties(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public static double getDoubleProperties(String key) {
        return Double.parseDouble(properties.getProperty(key));
    }

    public static float getFloatProperties(String key) {
        return Float.parseFloat(properties.getProperty(key));
    }
    public static long getLongProperties(String key) {
        return Long.parseLong(properties.getProperty(key));
    }
    public static byte getByteProperties(String key) {
        return Byte.parseByte(properties.getProperty(key));
    }

}
