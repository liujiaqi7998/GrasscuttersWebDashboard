package top.cyqi.Database;

import com.mongodb.client.result.DeleteResult;
import dev.morphia.query.experimental.filters.Filter;
import dev.morphia.query.experimental.filters.Filters;
import emu.grasscutter.database.DatabaseManager;
import emu.grasscutter.game.player.Player;

import java.util.List;

public class QiDatabaseHelper {

    public QiDatabaseHelper() {
    }

    //获取某名玩家的全部传送标记点
    public static List<TransferMemoData> getAllTransferMemoByUid(Player player) {
        return DatabaseManager.getGameDatastore().find(TransferMemoData.class).filter(new Filter[]{Filters.eq("ownerUid", player.getUid())}).stream().toList();
    }

    //获取传送标记点通过名字
    public static TransferMemoData getPlayerTransferMemoByName(Player player, String TransferName) {
        return DatabaseManager.getGameDatastore().find(TransferMemoData.class).filter(new Filter[]{
                Filters.eq("ownerUid", player.getUid()), Filters.eq("TransferName", TransferName)
        }).stream().findFirst().orElse(null);
    }

    //添加或更新传送标记点
    public static void addPlayerTransferMemo(TransferMemoData transferMemoData) {
        DatabaseManager.getGameDatastore().save(transferMemoData);
    }

    //删除传送标记点
    public static boolean deletePlayerTransferMemo(Player player, String TransferName) {
        TransferMemoData transferMemoData = getPlayerTransferMemoByName(player, TransferName);
        DeleteResult result =  DatabaseManager.getGameDatastore().delete(transferMemoData);
        return result.wasAcknowledged();
    }

    public static boolean deletePlayerAllTransferMemo(Player player) {
        List<TransferMemoData> transferMemoDataList = getAllTransferMemoByUid(player);
        for (TransferMemoData transferMemoData : transferMemoDataList) {
            DeleteResult result =  DatabaseManager.getGameDatastore().delete(transferMemoData);
            if (!result.wasAcknowledged()) {
                return false;
            }
        }
        return true;
    }
}
