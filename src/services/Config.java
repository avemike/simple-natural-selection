package services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Config class - controls all config keys-values within configs directory
 *
 * @todo elaborate on keeping all properties within one `static Properties` variable
 */
final public class Config {
    static public String configs_path = System.getProperty("user.dir") + "/configs";
    static Properties terrain_properties = new Properties();

    // Class is pseudo-static, there's no need to invoke constructor
    private Config() {
    }

    /**
     * Loads config from `fname` file into `properties`
     *
     * @param fname      - properties fileName (with extension e.g. `terrain.properties)
     * @param properties - variable which will store configs from specified file
     */
    public static void load(String fname, Properties properties) {
        FileInputStream is = null;

        try {
            is = new FileInputStream(configs_path + "/" + fname);
        } catch (FileNotFoundException e) {
            // @todo: make exception service handler
        }

        try {
            properties.load(is);
        } catch (IOException ex) {
            // @todo: make exception service handler
        }
    }

    public static Object terrain(String property) {
        // @todo: error handling
        if (terrain_properties.isEmpty()) load("terrain.properties", terrain_properties);

        return terrain_properties.get(property);
    }
}
