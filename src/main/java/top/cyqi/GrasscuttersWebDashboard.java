package top.cyqi;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.plugin.Plugin;
import emu.grasscutter.plugin.api.ServerHook;
import emu.grasscutter.server.dispatch.DispatchServer;
import emu.grasscutter.server.event.EventHandler;
import emu.grasscutter.server.event.HandlerPriority;
import emu.grasscutter.server.event.game.ServerTickEvent;
import emu.grasscutter.server.game.GameServer;
import emu.grasscutter.utils.Utils;
import top.cyqi.handlers.ServerTickHandler;
import top.cyqi.utils.GCGMUtils;
import top.cyqi.utils.PluginConfig;
import top.cyqi.utils.web.WebUtils;
import top.cyqi.websocket.WebSocketServer;

import java.io.*;
import java.util.stream.Collectors;

/**
 * 这是插件的主类
 */
public final class GrasscuttersWebDashboard extends Plugin {
    /* Turn the plugin into a singleton. */
    private static GrasscuttersWebDashboard INSTANCE;
    EventHandler<ServerTickEvent> serverTickEventHandler;
    private WebSocketServer webSocketServer;

    private PluginConfig configuration;

    @Override
    public void onLoad() {
        INSTANCE = this;

        // Get the configuration file.
        File config = new File(this.getDataFolder(), "GWDconfig.json");

        // Load the configuration.
        try {
            if(!config.exists() && !config.createNewFile()) {
                Grasscutter.getLogger().error("[WEB控制台] 配置文件不存在");
            } else {
                try (FileWriter writer = new FileWriter(config)) {
                    InputStream configStream = this.getResource("GWDconfig.json");
                    if(configStream == null) {
                        Grasscutter.getLogger().error("[WEB控制台] 无法储存默认配置文件");
                    } else {
                        writer.write(new BufferedReader(
                                new InputStreamReader(configStream)).lines().collect(Collectors.joining("\n"))
                        ); writer.close();
                        Grasscutter.getLogger().info("[WEB控制台] 已保存默认配置文件");
                    }
                }
            }

            // Put the configuration into an instance of the config class.
            this.configuration = Grasscutter.getGsonFactory().fromJson(new FileReader(config), PluginConfig.class);
        } catch (IOException exception) {
            Grasscutter.getLogger().error("[WEB控制台] 无法创建配置文件", exception);
        }

        WebUtils.PAGE_ROOT = "/Dashboard/" + this.configuration.token;
        serverTickEventHandler = new EventHandler<>(ServerTickEvent.class);
        serverTickEventHandler.listener(new ServerTickHandler());
        serverTickEventHandler.priority(HandlerPriority.HIGH);
        Grasscutter.getLogger().info("[WEB控制台] 加载中...");
    }

    @Override
    public void onEnable() {
        webSocketServer = new WebSocketServer();
        webSocketServer.start();
        serverTickEventHandler.register();
        Grasscutter.getLogger().info("[WEB控制台] 启动完成！！");
        Grasscutter.getLogger().info("[WEB控制台] 连接地址是：" + GCGMUtils.GetDispatchAddress() + WebUtils.PAGE_ROOT);
    }

    @Override
    public void onDisable() {
        webSocketServer.stop();
        Grasscutter.getLogger().info("[WEB控制台] 已停止");
    }

    public static GrasscuttersWebDashboard getInstance() {
        return INSTANCE;
    }

    public WebSocketServer getWebSocketServer() {
        return webSocketServer;
    }

    public static GameServer getGameServer() {
        return GrasscuttersWebDashboard.getInstance().getServer();
    }

    public static DispatchServer getDispatchServer() {
        return ServerHook.getInstance().getDispatchServer();
    }

}
