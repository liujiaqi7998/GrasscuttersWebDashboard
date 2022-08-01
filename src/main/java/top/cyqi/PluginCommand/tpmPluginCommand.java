package top.cyqi.PluginCommand;


import emu.grasscutter.command.Command;
import emu.grasscutter.command.CommandHandler;
import emu.grasscutter.game.player.Player;
import top.cyqi.Database.QiDatabaseHelper;
import top.cyqi.Database.TransferMemoData;

import java.util.List;

@Command(label = "tpm",
        usage = "tpm <go|add|del|delall|list> [Name]",
        aliases = {"tpm"},
        permission = "GrasscuttersWebDashboard.tpm")

public class tpmPluginCommand implements CommandHandler {
    public void execute(Player sender, Player targetPlayer, List<String> args) {
        if (targetPlayer == null) {
            CommandHandler.sendMessage(sender, "[tpm传送] 错误: 玩家UID不能为空");
            return;
        }

        String type;
        String name = "";
        if (args.size() == 1) {
            type = args.get(0);
            if (!type.equals("list")) {
                CommandHandler.sendMessage(sender, "[tpm传送] 错误: 参数错误，请使用/tpm <go|add|del|delall|list> [Name]");
                return;
            }
        } else if (args.size() == 2) {
            type = args.get(0);
            name = args.get(1);
            if (name.equals("")) {
                CommandHandler.sendMessage(sender, "[tpm传送] 错误: 传送点名称不能为空");
                return;
            }
        } else {
            CommandHandler.sendMessage(sender, "[tpm传送] 错误: 参数错误，请使用/tpm <go|add|del|delall|list> [Name]");
            return;
        }


        TransferMemoData data = new TransferMemoData(targetPlayer.getUid(), name, targetPlayer);
        switch (type) {
            case "add" -> {
                if (QiDatabaseHelper.getPlayerTransferMemoByName(targetPlayer, name) != null) {
                    CommandHandler.sendMessage(sender, "[tpm传送] 错误: 传送点 " + name + " 已存在，如需修改请先删除");
                    return;
                }
                QiDatabaseHelper.addPlayerTransferMemo(data);
                CommandHandler.sendMessage(sender, "[tpm传送] 成功: 传送点 " + name + " 已添加");
            }
            case "del" -> {
                if (QiDatabaseHelper.getPlayerTransferMemoByName(targetPlayer, name) == null) {
                    CommandHandler.sendMessage(sender, "[tpm传送] 错误: 传送点 " + name + " 不存在，查看全部传送点请使用/tpm list");
                    return;
                }
                if (QiDatabaseHelper.deletePlayerTransferMemo(targetPlayer, name)) {
                    CommandHandler.sendMessage(sender, "[tpm传送] 删除成功: 传送点 " + name + " 已删除");
                } else {
                    CommandHandler.sendMessage(sender, "[tpm传送] 错误: 传送点 " + name + " 删除失败");
                }
            }
            case "go" -> {
                TransferMemoData transferMemoData = QiDatabaseHelper.getPlayerTransferMemoByName(targetPlayer, name);
                if (transferMemoData == null) {
                    CommandHandler.sendMessage(sender, "[tpm传送] 错误: 传送点 " + name + " 不存在，查看全部传送点请使用/tpm list");
                    return;
                }
                Player playerState = transferMemoData.getSavePlayerState();
                if (playerState == null) {
                    CommandHandler.sendMessage(sender, "[tpm传送] 错误: 传送点 " + name + " 不存在，查看全部传送点请使用/tpm list");
                    return;
                }

                int SceneId = playerState.getSceneId();

                boolean result = targetPlayer.getWorld().transferPlayerToScene(targetPlayer, SceneId, playerState.getPosition());
                if (result) {
                    CommandHandler.sendMessage(sender, "[tpm传送] 传送成功: 传送点 " + name + " 已传送");
                } else {
                    CommandHandler.sendMessage(sender, "[tpm传送] 错误: 传送点 " + name + " 传送失败");
                }
            }
            case "delall" -> {
                if (name.equals("yes")) {
                    if (QiDatabaseHelper.deletePlayerAllTransferMemo(targetPlayer)) {
                        CommandHandler.sendMessage(sender, "[tpm传送] 删除成功: 全部传送点已删除");
                    }else{
                        CommandHandler.sendMessage(sender, "[tpm传送] 错误: 全部传送点删除失败");
                    }
                }else{
                    CommandHandler.sendMessage(sender, "[tpm传送] 错误: 全部删除，请使用/tpm delall yes");
                }

            }
            case "list" -> {
                List<TransferMemoData> list = QiDatabaseHelper.getAllTransferMemoByUid(targetPlayer);
                if (list.size() == 0) {
                    CommandHandler.sendMessage(sender, "[tpm传送] 传送点列表: 空");
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (TransferMemoData data1 : list) {
                    sb.append("\n").append(data1.getTransferName());
                }
                CommandHandler.sendMessage(sender, "[tpm传送] 传送点列表: " + sb);
            }
            default -> CommandHandler.sendMessage(sender, "[tpm传送] 错误: 未知指令'" + type + "'，请使用/tpm <go|add|del|delall|list> [Name]");
        }

    }
}
