package xyz.grasscutters.pltm;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.plugin.Plugin;
import emu.grasscutter.server.event.EventHandler;
import emu.grasscutter.server.event.HandlerPriority;
import emu.grasscutter.server.event.player.PlayerJoinEvent;
import org.slf4j.Logger;
import xyz.grasscutters.pltm.objects.PluginConfig;

import java.io.*;
import java.util.stream.Collectors;

/**
 * The Grasscutter plugin template.
 * This is the main class for the plugin.
 */
public final class PluginTemplate extends Plugin {
    /* Turn the plugin into a singleton. */
    private static PluginTemplate instance;

    /**
     * Gets the plugin instance.
     * @return A plugin singleton.
     */
    public static PluginTemplate getInstance() {
        return instance;
    }
    
    /* A proper plugin logger will be introduced in v1.0.4-dev. */
    private final Logger logger = Grasscutter.getLogger();
    /* The plugin's configuration instance. */
    private PluginConfig configuration;
    
    /**
     * This method is called immediately after the plugin is first loaded into system memory.
     */
    @Override public void onLoad() {
        // Set the plugin instance.
        instance = this;
        
        // Get the configuration file.
        File config = new File(this.getDataFolder(), "config.json");
        
        // Load the configuration.
        try {
            if(!config.exists() && !config.createNewFile()) {
                this.logger.error("Failed to create config file.");
            } else {
                try (FileWriter writer = new FileWriter(config)) {
                    InputStream configStream = this.getResource("config.json");
                    if(configStream == null) {
                        this.logger.error("Failed to save default config file.");
                    } else {
                        writer.write(new BufferedReader(
                                new InputStreamReader(configStream)).lines().collect(Collectors.joining("\n"))
                        ); writer.close();
                        
                        this.logger.info("Saved default config file.");
                    }
                }
            }

            // Put the configuration into an instance of the config class.
            this.configuration = Grasscutter.getGsonFactory().fromJson(new FileReader(config), PluginConfig.class);
        } catch (IOException exception) {
            this.logger.error("Failed to create config file.", exception);
        }
    }

    /**
     * This method is called before the servers are started, or when the plugin enables.
     */
    @Override public void onEnable() {
        // Register event listeners.
        new EventHandler<>(PlayerJoinEvent.class)
                .priority(HandlerPriority.LOW)
                .listener(EventListeners::onJoin)
                .register();
    }

    /**
     * This method is called when the plugin is disabled.
     */
    @Override public void onDisable() {
        
    }

    /**
     * Gets the plugin's configuration.
     * @return A plugin config instance.
     */
    public PluginConfig getConfiguration() {
        return this.configuration;
    }
}
