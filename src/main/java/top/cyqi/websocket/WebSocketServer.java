package top.cyqi.websocket;

import emu.grasscutter.Grasscutter;
import express.Express;
import io.javalin.websocket.WsContext;
import top.cyqi.GrasscuttersWebDashboard;
import top.cyqi.utils.web.WebUtils;
import top.cyqi.websocket.json.WSData;
import top.cyqi.websocket.json.WsMsg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketServer {

    //用于保存所有的连接的的Map
    public static Map<WsContext, String> ClitenContextMap = new ConcurrentHashMap<>();
    Express app = GrasscuttersWebDashboard.getDispatchServer().getServer();

    public void start() {
        app.ws(WebUtils.PAGE_ROOT, ws -> {
            ws.onConnect(ctx -> {
                String ws_id = WebUtils.getUUID();
                ClitenContextMap.put(ctx, ws_id);
                Grasscutter.getLogger().info("[WEB控制台] 用户登录到了Web控制台，ID：" + ws_id);
            });

            ws.onMessage(wsMessageContext -> {
                //获取消息
                String Ws_Msg = wsMessageContext.message();
                WsMsg wsMsg;
                try {
                    wsMsg = Grasscutter.getGsonFactory().fromJson(Ws_Msg, WsMsg.class);
                } catch (Exception e) {
                    wsMessageContext.send(new WSData("tip", "消息格式处理异常"));
                    e.printStackTrace();
                    return;
                }

                //判断消息类型是否为空
                if (wsMsg.type == null || wsMsg.type.equals("")) {
                    wsMessageContext.send(new WSData("tip", "消息类型为空"));
                    return;
                }
                //判断数据是否为空
                if (wsMsg.data == null || wsMsg.data.equals("")) {
                    wsMessageContext.send(new WSData("tip", "数据为空"));
                    return;
                }
                ServerUtils.DealMessage(wsMsg.type, wsMsg.data, wsMessageContext);
            });

            ws.onClose(ctx -> {
                String ws_id = ClitenContextMap.get(ctx);
                ClitenContextMap.remove(ctx);
                Grasscutter.getLogger().info("[WEB控制台] 用户离开了Web控制台，ID：" + ws_id);
            });
        });
    }


    public void stop() {
        //清除用户列表，关闭连接
        ClitenContextMap.clear();
    }

    public void broadcast(WSData data) {
        ClitenContextMap.keySet().stream().filter(ctx -> ctx.session.isOpen()).forEach(session -> session.send(data));
    }
}
