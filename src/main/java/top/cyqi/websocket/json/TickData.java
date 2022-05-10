package top.cyqi.websocket.json;

/* 心跳包数据 */
public class TickData {
    /* 距上一次获取时间 */
    public long tickTimeElapsed;
    public long serverUptime;
    /* 空闲内存信息 */
    public long getFreeMemory;
    /* 全部内存信息 */
    public long getAllocatedMemory;
    /* 玩家数量 */
    public int playerCount;
}
