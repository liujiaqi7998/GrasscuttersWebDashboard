package top.cyqi.websocket;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.command.CommandMap;
import emu.grasscutter.game.player.Player;
import io.javalin.websocket.WsMessageContext;
import top.cyqi.GrasscuttersWebDashboard;
import top.cyqi.websocket.json.BaseData;
import top.cyqi.websocket.json.PlayerData;
import top.cyqi.websocket.json.WSData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ServerUtils {
    static void DealMessage(String type, String data, WsMessageContext wsMessageContext) {
        String ws_id = WebSocketServer.ClitenContextMap.get(wsMessageContext);
        switch (type) {
            case "CMD" -> {
                Grasscutter.getLogger().info("[WEB控制台] 执行" + ws_id + "的命令:" + data);
                CommandMap commandMap = Grasscutter.getGameServer().getCommandMap();
                commandMap.invoke(null, null, data);
            }
            case "State" -> {
                wsMessageContext.send(new WSData("BaseData", GrasscuttersWebDashboard.baseData));
            }
            case "Player" -> {
                Map<Integer, Player> playerMap = GrasscuttersWebDashboard.getGameServer().getPlayers();
                //循环遍历所有玩家
                List<PlayerData> playerDatas = new ArrayList<>();
                for (Map.Entry<Integer, Player> entry : playerMap.entrySet()) {
                    Player player = entry.getValue();
                    PlayerData playerData = new PlayerData();
                    playerData.uid = player.getUid();
                    playerData.signature = player.getSignature();
                    playerData.nickname = player.getNickname();
                    playerData.headImage = player.getHeadImage();
                    playerData.worldLevel = player.getWorldLevel();
                    playerData.Level = player.getLevel();
                    playerData.pos = player.getPos().toString();
                    playerDatas.add(playerData);
                }
                wsMessageContext.send(new WSData("PlayerList", playerDatas));
            }
        }
    }
}
