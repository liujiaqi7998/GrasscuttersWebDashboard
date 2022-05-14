package top.cyqi;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import emu.grasscutter.Grasscutter;
import emu.grasscutter.command.CommandMap;
import emu.grasscutter.plugin.Plugin;
import emu.grasscutter.plugin.api.ServerHook;
import emu.grasscutter.server.event.EventHandler;
import emu.grasscutter.server.event.HandlerPriority;
import emu.grasscutter.server.event.game.ServerTickEvent;
import emu.grasscutter.server.game.GameServer;
import emu.grasscutter.server.http.HttpServer;
import emu.grasscutter.utils.ConfigContainer;
import top.cyqi.PluginCommand.tpmPluginCommand;
import top.cyqi.handlers.ServerTickHandler;
import top.cyqi.utils.Utils;
import top.cyqi.PluginCommand.webtoolsPluginCommand;
import top.cyqi.utils.PluginConfig;
import top.cyqi.handlers.WebConsoleListAppender;
import top.cyqi.utils.WebUtils;
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
        baseData.ServerName = getServerConfig().server.dispatch.defaultName;
        //获取JAVA版本
        baseData.JavaVersion = System.getProperty("java.version");
        //获取IP地址
        baseData.IP = (GrasscuttersWebDashboard.getServerConfig().server.http.accessAddress.isEmpty() ? GrasscuttersWebDashboard.getServerConfig().server.http.bindAddress : GrasscuttersWebDashboard.getServerConfig().server.http.accessAddress) +
                ":" + (GrasscuttersWebDashboard.getServerConfig().server.http.accessPort != 0 ? GrasscuttersWebDashboard.getServerConfig().server.http.accessPort : GrasscuttersWebDashboard.getServerConfig().server.http.bindPort);
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
        CommandMap.getInstance().registerCommand("webtools", new webtoolsPluginCommand());
        CommandMap.getInstance().registerCommand("tpm", new tpmPluginCommand());
        Grasscutter.getLogger().info("[WEB控制台] 启动完成！！");
        Grasscutter.getLogger().info("[WEB控制台] 您设置的Token是：" + this.configuration.token);
        Grasscutter.getLogger().info("[WEB控制台] 连接地址是：" + Utils.GetDispatchAddress() + WebUtils.PAGE_ROOT);
        Grasscutter.getLogger().info("[WEB控制台] 快速连接，用浏览器打开：" + "https://liujiaqi7998.github.io/GrasscuttersWebDashboard/index.html?server=" + Utils.GetDispatchAddress() + WebUtils.PAGE_ROOT);
        Grasscutter.getLogger().info("[WEB控制台] 连接地址不要告诉任何人，建议定期更换token！！");
    }

    @Override
    public void onDisable() {
        webSocketServer.stop();
        CommandMap.getInstance().unregisterCommand("webtools");
        CommandMap.getInstance().unregisterCommand("tpm");
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

    public static ConfigContainer getServerConfig() {
        return Grasscutter.getConfig();
    }

    public static HttpServer getDispatchServer() {
        return ServerHook.getInstance().getHttpServer();
    }

}
