package top.cyqi.websocket.json;

/* websocket 预发送数据 */
public class WSData {
    public String eventName;
    public Object data;

    public WSData(String eventName, Object data) {
        this.eventName = eventName;
        this.data = data;
    }
}
