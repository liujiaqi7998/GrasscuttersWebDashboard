package top.cyqi.websocket;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.command.CommandMap;
import emu.grasscutter.database.DatabaseHelper;
import emu.grasscutter.game.player.Player;
import emu.grasscutter.game.quest.GameMainQuest;
import io.javalin.websocket.WsMessageContext;
import top.cyqi.GrasscuttersWebDashboard;
import top.cyqi.handlers.WebToolsMessageHandler;
import top.cyqi.Database.QiDatabaseHelper;
import top.cyqi.Database.TransferMemoData;
import top.cyqi.websocket.json.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServerUtils {

    public static Map<String, PlayerWebKey> tempPlayersData = new HashMap<>();

    static void DealMessage(String type, String data, WsMessageContext wsMessageContext) {
        String ws_id = WebSocketServer.ClitenContextMap.get(wsMessageContext);
        switch (type) {
            case "CMD" -> {
                Grasscutter.getLogger().info("[WEB控制台] 执行" + ws_id + "的命令:" + data);
                CommandMap commandMap = Grasscutter.getGameServer().getCommandMap();
                commandMap.invoke(null, null, data);
            }
            case "State" -> wsMessageContext.send(new WSData("BaseData", GrasscuttersWebDashboard.baseData));
            case "Player" -> showPlayerList(wsMessageContext);
        }
    }

    public static void showPlayerList(WsMessageContext wsMessageContext) {
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
            playerData.pos = player.getPosition().toString();
            playerData.SceneId = player.getSceneId();
            playerDatas.add(playerData);
        }
        wsMessageContext.send(new WSData("PlayerList", playerDatas));
    }

    public static void WebToolsDealMessage(Player player, String type, String data, WsMessageContext wsMessageContext) {
        switch (type) {
            case "CMD" -> {
                WebToolsMessageHandler resultCollector = new WebToolsMessageHandler();
                resultCollector.wsMessageContext = wsMessageContext;
                resultCollector.player = player;
                player.setMessageHandler(resultCollector);
                CommandMap commandMap = Grasscutter.getGameServer().getCommandMap();
                try {
                    commandMap.invoke(player, player, data);
                } catch (Exception e) {
                    wsMessageContext.send(new WSData("error", e.getMessage()));
                }

            }
            case "TPMList" -> showTPMList(player, wsMessageContext);
            case "Player" -> showPlayerList(wsMessageContext);
            case "DelQuest" -> {
                List<GameMainQuest> gameMainQuests = DatabaseHelper.getAllQuests(player);
                //遍历所有任务
                for (GameMainQuest gameMainQuest : gameMainQuests) {
                    //如果任务id相同,或者全部删除
                    if (data.equals("ALL") || data.contains(gameMainQuest.getParentQuestId() + "")) {
                        //删除任务
                        DatabaseHelper.deleteQuest(gameMainQuest);
                        wsMessageContext.send(new WSData("success", "删除'" + gameMainQuest.getParentQuestId() + "'成功，请退出重进"));
                        if (!data.equals("ALL")) {
                            return;
                        }
                    }
                }
                wsMessageContext.send(new WSData("error", "任务不存在"));
            }
        }
    }

    private static void showTPMList(Player player, WsMessageContext wsMessageContext) {
        List<TransferMemoData> tpmDatas = QiDatabaseHelper.getAllTransferMemoByUid(player);
        List<TransferData> SendtpmDatas = new ArrayList<>();
        for (TransferMemoData tpm : tpmDatas) {
            TransferData transferData = new TransferData();
            transferData.uid = String.valueOf(tpm.getOwnerUid());
            transferData.Name = tpm.getTransferName();
            transferData.SceneId = String.valueOf(tpm.getSavePlayerState().getSceneId());
            transferData.pos = tpm.getSavePlayerState().getPosition().toString();
            SendtpmDatas.add(transferData);
        }
        wsMessageContext.send(new WSData("TPMList", SendtpmDatas));
    }
}
