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
public final class Config {
    private static final Properties properties = new Properties();
    public static String configs_path = System.getProperty("user.dir") + "/configs";
    public static String assets_path = System.getProperty("user.dir") + "/assets";
    public static String[] properties_fnames = {"animals", "terrain", "gui", "simulation", "plants"};

    // Class is pseudo-static, there's no need to invoke constructor
    private Config() {
    }

    /**
     * Loads config from `fname` file into `properties`
     *
     * @param fname - properties fileName (with extension e.g. `terrain.properties)
     */
    public static void load(String fname) {
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

    public static void loadAll() {
        for (String properties_fname : properties_fnames)
            load(properties_fname + ".properties");
    }

    public static String get(String property) {
        if (properties.isEmpty()) loadAll();

        final var value = properties.get(property);

        if (value == null) return "";
        // @todo: elaborate on it - throw new Exception(String.format("Config: There is no %s property", property));

        return value.toString();
    }
}
