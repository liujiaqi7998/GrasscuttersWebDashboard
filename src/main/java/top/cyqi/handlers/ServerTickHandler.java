package top.cyqi.handlers;

import emu.grasscutter.server.event.game.ServerTickEvent;
import emu.grasscutter.utils.EventConsumer;
import top.cyqi.GrasscuttersWebDashboard;
import top.cyqi.utils.GCGMUtils;
import top.cyqi.websocket.json.TickData;
import top.cyqi.websocket.json.WSData;

import java.time.Instant;

//循环更新前端数据

public class ServerTickHandler implements EventConsumer<ServerTickEvent> {
    private static Instant firstTick;
    private static Instant lastTick;

    @Override
    public void consume(ServerTickEvent serverTickEvent) {
        if(lastTick != null) {
            Instant now = Instant.now();
            TickData data = new TickData();
            data.tickTimeElapsed = now.toEpochMilli() - lastTick.toEpochMilli();
            data.serverUptime = lastTick.toEpochMilli() - firstTick.toEpochMilli();
            data.getFreeMemory = GCGMUtils.GetFreeJVMMemory();
            data.getAllocatedMemory = GCGMUtils.GetAllocatedJVMMemory();
            data.playerCount = GrasscuttersWebDashboard.getGameServer().getPlayers().size();
            GrasscuttersWebDashboard.getInstance().getWebSocketServer().broadcast(new WSData("tick", data));
            lastTick = now;
        } else {
            lastTick = Instant.now();
            firstTick = Instant.now();
        }
    }
}
