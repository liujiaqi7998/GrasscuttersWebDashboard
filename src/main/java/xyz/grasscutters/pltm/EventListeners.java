package xyz.grasscutters.pltm;

import emu.grasscutter.game.player.Player;
import emu.grasscutter.server.event.player.PlayerJoinEvent;
import xyz.grasscutters.pltm.objects.PluginConfig;

/**
 * A class containing all event handlers.
 */
public final class EventListeners {
    private static final PluginConfig config = PluginTemplate.getInstance().getConfiguration();
    
    /**
     * Called when the player joins the server.
     * @param event PlayerJoinEvent.
     */
    public static void onJoin(PlayerJoinEvent event) {
        if(!config.sendJoinMessage) return;
        
        Player player = event.getPlayer();
        player.dropMessage(config.joinMessage);
    }
}
