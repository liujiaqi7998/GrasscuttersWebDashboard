package top.cyqi.websocket.json;

public class BaseData {
    public static String IP;
    /* 系统版本 */
    public static String SystemVersion;
    /* 计算机名称 */
    public static String ServerName;
    /* JAVA版本 */
    public static String JavaVersion;
    /* 割草机版本 */
    public static String GrVersion;

    public static String getJSON() {
        String json = "{";
        json += "\"IP\":\"" + IP + "\",";
        json += "\"SystemVersion\":\"" + SystemVersion + "\",";
        json += "\"ServerName\":\"" + ServerName + "\",";
        json += "\"JavaVersion\":\"" + JavaVersion + "\"";
        json += "\"GrVersion\":\"" + GrVersion + "\"";
        json += "}";
        return json;
    }
}
