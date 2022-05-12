package top.cyqi;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import emu.grasscutter.Config;
import emu.grasscutter.Grasscutter;
import emu.grasscutter.command.CommandMap;
import emu.grasscutter.plugin.Plugin;
import emu.grasscutter.plugin.api.ServerHook;
import emu.grasscutter.server.dispatch.DispatchServer;
import emu.grasscutter.server.event.EventHandler;
import emu.grasscutter.server.event.HandlerPriority;
import emu.grasscutter.server.event.game.ServerTickEvent;
import emu.grasscutter.server.game.GameServer;
import top.cyqi.handlers.ServerTickHandler;
import top.cyqi.utils.GCGMUtils;
import top.cyqi.utils.PluginCommand;
import top.cyqi.utils.PluginConfig;
import top.cyqi.utils.WebConsoleListAppender;
import top.cyqi.utils.web.WebUtils;
import top.cyqi.websocket.WebSocketServer;
import top.cyqi.websocket.json.BaseData;

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

    public static BaseData baseData;

    public static int key_timeout;

    @Override
    public void onLoad() {
        INSTANCE = this;

        // Get the configuration file.
        File config = new File(this.getDataFolder(), "GWDconfig.json");

        // Load the configuration.
        try {
            if (!config.exists()) {
                if (config.createNewFile()) {
                    Grasscutter.getLogger().error("[WEB控制台] 配置文件不存在");
                    FileWriter writer = new FileWriter(config);
                    InputStream configStream = this.getResource("GWDconfig.json");
                    writer.write(new BufferedReader(
                            new InputStreamReader(configStream)).lines().collect(Collectors.joining("\n"))
                    );
                    writer.close();
                    Grasscutter.getLogger().info("[WEB控制台] 配置文件为空，已保存默认配置文件");
                }
            }
            // Put the configuration into an instance of the config class.
            this.configuration = Grasscutter.getGsonFactory().fromJson(new FileReader(config), PluginConfig.class);
        } catch (IOException exception) {
            Grasscutter.getLogger().error("[WEB控制台] 配置文件读取错误", exception);
            return;
        }


        key_timeout = configuration.key_timeout;//读取配置文件中的key超时时间
        WebUtils.PAGE_ROOT = "/Dashboard/" + this.configuration.token;
        WebUtils.WebtoolsURL = this.configuration.WebtoolsURL;

        serverTickEventHandler = new EventHandler<>(ServerTickEvent.class);
        serverTickEventHandler.listener(new ServerTickHandler());
        serverTickEventHandler.priority(HandlerPriority.HIGH);
        //获取系统版本计算机名称等信息
        baseData = new BaseData();
        baseData.SystemVersion = System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch");
        baseData.ServerName = getServerConfig().GameServer.ServerNickname;
        //获取JAVA版本
        baseData.JavaVersion = System.getProperty("java.version");
        //获取IP地址
        baseData.IP = getServerConfig().GameServer.Ip;
        baseData.GrVersion = getVersion();
        Grasscutter.getLogger().info("[WEB控制台] 加载中...");
    }

    @Override
    public void onEnable() {
        webSocketServer = new WebSocketServer();
        webSocketServer.start();
        serverTickEventHandler.register();
        // Register event listeners.

        // create and start a ListAppender
        ListAppender<ILoggingEvent> listAppender = new WebConsoleListAppender<>();
        listAppender.start();
        listAppender.setName("WebConsole");
        listAppender.start();
        Grasscutter.getLogger().addAppender(listAppender);
        CommandMap.getInstance().registerCommand("webtools", new PluginCommand());
        Grasscutter.getLogger().info("[WEB控制台] 启动完成！！");
        Grasscutter.getLogger().info("[WEB控制台] 您设置的Token是：" + this.configuration.token);
        Grasscutter.getLogger().info("[WEB控制台] 连接地址是：" + GCGMUtils.GetDispatchAddress() + WebUtils.PAGE_ROOT);
        Grasscutter.getLogger().info("[WEB控制台] 连接地址不要告诉任何人，建议定期更换！！");
    }

    @Override
    public void onDisable() {
        webSocketServer.stop();
        CommandMap.getInstance().unregisterCommand("webtools");
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

    public static Config getServerConfig() {
        return Grasscutter.getConfig();
    }

    public static DispatchServer getDispatchServer() {
        return ServerHook.getInstance().getDispatchServer();
    }

}
