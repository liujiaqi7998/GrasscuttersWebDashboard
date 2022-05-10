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
import top.cyqi.utils.web.WebUtils;
import top.cyqi.websocket.WebSocketServer;

import java.io.*;

/**
 * 这是插件的主类
 */
public final class GrasscuttersWebDashboard extends Plugin {
    /* Turn the plugin into a singleton. */
    private static GrasscuttersWebDashboard INSTANCE;
    EventHandler<ServerTickEvent> serverTickEventHandler;
    private WebSocketServer webSocketServer;
    private File webData;

    @Override
    public void onLoad() {
        INSTANCE = this;
        webData = new File(Utils.toFilePath(getDataFolder().getPath() + "/www"));
        serverTickEventHandler = new EventHandler<>(ServerTickEvent.class);
        serverTickEventHandler.listener(new ServerTickHandler());
        serverTickEventHandler.priority(HandlerPriority.HIGH);
        File pluginDataDir = getDataFolder();


        if (!pluginDataDir.exists() && !pluginDataDir.mkdirs()) {
            Grasscutter.getLogger().error("[WEB控制台] 未能创建插件数据目录目录: " + pluginDataDir.getAbsolutePath());
            return;
        }
        if (!webData.exists()) {
            /* 判断面板WEB文件是否存在逻辑*/
            Grasscutter.getLogger().warn("[WEB控制台] '" + webData.getAbsolutePath() + "' 目录不存在，正在创建...");
            String zipFileLoc = Utils.toFilePath(getDataFolder().getPath() + "/DefaultWebApp.zip");
            if (!new File(zipFileLoc).exists()) {
                if (GCGMUtils.CopyFile("DefaultWebApp.zip", zipFileLoc)) {
                    Grasscutter.getLogger().warn("[WEB控制台] 无法找到DefaultWebApp.zip压缩文件，请手动解压文件 'DefaultWebApp.zip' 到 '" + webData.getAbsolutePath() + "' 目录，然后重启服务器");
                } else {
                    Grasscutter.getLogger().error("[WEB控制台] 初始化错误.");
                    return;
                }
            }
        }
        Grasscutter.getLogger().info("[WEB控制台] 加载完毕...");
    }

    @Override
    public void onEnable() {
        if (webData.exists()) {
            WebUtils.addStaticFiles(webData);
            webSocketServer = new WebSocketServer();
            webSocketServer.start();
            serverTickEventHandler.register();
            Grasscutter.getLogger().info("[WEB控制台] 启动完成！！");
            Grasscutter.getLogger().info("[WEB控制台] 面板访问地址：" + GCGMUtils.GetDispatchAddress() + WebUtils.PAGE_ROOT);
        } else {
            Grasscutter.getLogger().error("[WEB控制台] 无法在其插件目录中找到 www 文件夹");
            Grasscutter.getLogger().error("[WEB控制台] 请解压文件 'DefaultWebApp.zip' 到 '" + webData.getAbsolutePath() + "' 目录");
            Grasscutter.getLogger().error("[WEB控制台] 无法启动");
        }
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
