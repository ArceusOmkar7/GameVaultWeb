package gamevaultbase.config;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Configuration handler for dummy data generation.
 * Manages the configuration file that tracks whether dummy data has been
 * loaded.
 */
public class DummyDataConfig {
    private static final String CONFIG_FILE_PATH = "/WEB-INF/dummy_data_config.txt";
    private final ServletContext servletContext;
    private Properties config;

    public DummyDataConfig(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Loads the configuration from the config file.
     * Creates the file with default values if it doesn't exist.
     */
    public Properties loadConfiguration() throws IOException {
        config = new Properties();
        String fullPath = servletContext.getRealPath(CONFIG_FILE_PATH);
        File configFile = new File(fullPath);

        // Create the config file if it doesn't exist
        if (!configFile.exists()) {
            // Create parent directories if needed
            if (!configFile.getParentFile().exists()) {
                configFile.getParentFile().mkdirs();
            }

            // Initialize with default values
            config.setProperty("dummy_data.loaded", "false");
            config.setProperty("games.count", "50");
            config.setProperty("users.count", "10");
            config.setProperty("orders.count", "50");
            config.setProperty("last_load_date", "");

            // Save the initial config
            try (FileOutputStream fos = new FileOutputStream(configFile)) {
                config.store(fos, "Initial Dummy Data Configuration");
            }
        } else {
            // Load existing config
            try (FileInputStream fis = new FileInputStream(configFile)) {
                config.load(fis);
            }
        }

        return config;
    }

    /**
     * Updates the configuration file after data generation.
     */
    public void updateConfiguration() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());

        config.setProperty("dummy_data.loaded", "true");
        config.setProperty("last_load_date", currentDate);

        String fullPath = servletContext.getRealPath(CONFIG_FILE_PATH);
        try (FileOutputStream fos = new FileOutputStream(fullPath)) {
            config.store(fos, "Dummy Data Configuration - Last Updated: " + currentDate);
        }
    }

    /**
     * Checks if data has already been loaded.
     */
    public boolean isDataLoaded() {
        return Boolean.parseBoolean(config.getProperty("dummy_data.loaded", "false"));
    }

    /**
     * Gets the last load date.
     */
    public String getLastLoadDate() {
        return config.getProperty("last_load_date", "");
    }

    /**
     * Gets the configured number of games to generate.
     */
    public int getGamesCount() {
        return Integer.parseInt(config.getProperty("games.count", "50"));
    }

    /**
     * Gets the configured number of users to generate.
     */
    public int getUsersCount() {
        return Integer.parseInt(config.getProperty("users.count", "10"));
    }

    /**
     * Gets the configured number of orders to generate.
     */
    public int getOrdersCount() {
        return Integer.parseInt(config.getProperty("orders.count", "50"));
    }
}