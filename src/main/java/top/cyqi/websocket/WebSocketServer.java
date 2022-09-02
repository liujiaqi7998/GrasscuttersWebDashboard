package top.cyqi.websocket;

import com.google.gson.Gson;
import emu.grasscutter.Grasscutter;
import emu.grasscutter.game.player.Player;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import top.cyqi.GrasscuttersWebDashboard;
import top.cyqi.utils.WebUtils;
import top.cyqi.websocket.json.PlayerWebKey;
import top.cyqi.websocket.json.WSData;
import top.cyqi.websocket.json.WsMsg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketServer {

    //用于保存所有的连接的的Map
    public static Map<WsContext, String> ClitenContextMap = new ConcurrentHashMap<>();

    public static Map<WsContext, String> WebToolsClitenContextMap = new ConcurrentHashMap<>();
    Javalin app = GrasscuttersWebDashboard.getDispatchServer().getHandle();

    public void start() {

        app.ws(WebUtils.WebToolsPAGE_ROOT, ws -> {
            ws.onConnect(ctx -> {
                String key = ctx.queryParam("key");
                if (key == null) {
                    ctx.send(new WSData("auth", "密钥不能为空"));
                    ctx.session.close(1700, "密钥不能为空");
                    return;
                }
                //判断key是否是8位
                if (key.length() != 8) {
                    ctx.send(new WSData("auth", "密钥格式不正确"));
                    ctx.session.close(1701, "密钥格式不正确");
                    return;
                }
                //判断tempPlayersData是否包含key
                if (!ServerUtils.tempPlayersData.containsKey(key)) {
                    ctx.send(new WSData("auth", "密钥不存在,或者已经过期"));
                    ctx.session.close(1702, "密钥不存在,或者已经过期");
                    return;
                }
                //判断密钥是否超过GrasscuttersWebDashboard.key_timeout分钟
                if (System.currentTimeMillis() - ServerUtils.tempPlayersData.get(key).getTime > 3 * 60 * 1000) {
                    ServerUtils.tempPlayersData.remove(key);
                    ctx.send(new WSData("auth", "密钥已过期，请重新获取"));
                    ctx.session.close(1703, "密钥已过期，请重新获取");
                    return;
                }
                WebToolsClitenContextMap.put(ctx, key);
                ctx.send(new WSData("auth", "连接成功"));
                Grasscutter.getLogger().info("[WebTools] UID:'" + ServerUtils.tempPlayersData.get(key).player.getUid() + "'登录到了Web工具箱");
            });

            ws.onMessage(wsMessageContext -> {
                PlayerWebKey NowPlayerWebKey = ServerUtils.tempPlayersData.get(WebToolsClitenContextMap.get(wsMessageContext));
                Player NowPlayer = NowPlayerWebKey.getPlayer();
                //判断玩家是否在线
                Map<Integer, Player> playerMap = GrasscuttersWebDashboard.getGameServer().getPlayers();
                if (!playerMap.containsKey(NowPlayer.getUid())) {
                    wsMessageContext.send(new WSData("error", "玩家已经下线"));
                    wsMessageContext.session.close();
                    return;
                }
                //获取消息
                String Ws_Msg = wsMessageContext.message();
                //延长验证码时间
                NowPlayerWebKey.setTime(System.currentTimeMillis());

                WsMsg wsMsg;
                try {
                    wsMsg = new Gson().fromJson(Ws_Msg, WsMsg.class);
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
                ServerUtils.WebToolsDealMessage(NowPlayer, wsMsg.type, wsMsg.data, wsMessageContext);

            });

        });

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
                    wsMsg = new Gson().fromJson(Ws_Msg, WsMsg.class);
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
        //清除网页控制台的用户列表，关闭连接
        ClitenContextMap.clear();
    }

    public void broadcast(WSData data) {
        ClitenContextMap.keySet().stream().filter(ctx -> ctx.session.isOpen()).forEach(session -> session.send(data));
    }
}
