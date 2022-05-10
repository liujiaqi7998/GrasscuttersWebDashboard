package top.cyqi.websocket;

import emu.grasscutter.Grasscutter;
import express.Express;
import io.javalin.websocket.WsContext;
import top.cyqi.GrasscuttersWebDashboard;
import top.cyqi.websocket.json.WSData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketServer {

    //SocketIOServer socketIOServer;
    private static final Map<WsContext, String> userUsernameMap = new ConcurrentHashMap<>();

    public void start() {
        Express app = GrasscuttersWebDashboard.getDispatchServer().getServer();

        app.ws("/gm", ws -> {
            ws.onConnect(ctx -> {
                String username = "Not logged in";
                userUsernameMap.put(ctx, username);
                Grasscutter.getLogger().info("[GCGM] User logged in to panel");
            });
        });
    }

    public void stop() {
        //socketIOServer.stop();

    }
    public void broadcast(WSData data) {
        userUsernameMap.keySet().stream().filter(ctx -> ctx.session.isOpen()).forEach(session -> {
            session.send(data);
        });
    }
}
