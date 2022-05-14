package top.cyqi.Database;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Indexed;
import dev.morphia.annotations.Transient;
import emu.grasscutter.game.player.Player;
import emu.grasscutter.game.quest.GameQuest;
import emu.grasscutter.game.quest.enums.ParentQuestState;
import emu.grasscutter.utils.Position;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Map;

@Entity(
        value = "TransferMemoData",
        useDiscriminator = false
)

public class TransferMemoData {

    @Id
    private ObjectId id;
    @Indexed
    private int ownerUid;
    private String TransferName;
    private Player SavePlayerState;

    public TransferMemoData(){}

    public TransferMemoData(int ownerUid, String TransferName, Player SavePlayerState){
        this.ownerUid = ownerUid;
        this.TransferName = TransferName;
        this.SavePlayerState = SavePlayerState;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(int ownerUid) {
        this.ownerUid = ownerUid;
    }

    public String getTransferName() {
        return TransferName;
    }

    public void setTransferName(String TransferName) {
        this.TransferName = TransferName;
    }

    public Player getSavePlayerState() {
        return SavePlayerState;
    }

    public void setSavePlayerState(Player SavePlayerState) {
        this.SavePlayerState = SavePlayerState;
    }
}

