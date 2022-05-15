package top.cyqi.websocket.json;

import emu.grasscutter.game.player.Player;

public class PlayerWebKey {
    public Player player;
    public String Key;
    public long getTime;

    public Player getPlayer() {
        return player;
    }

    public void setTime(long currentTimeMillis) {
        this.getTime = currentTimeMillis;
    }
}
