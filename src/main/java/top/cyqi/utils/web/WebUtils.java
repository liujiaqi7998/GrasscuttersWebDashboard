package top.cyqi.utils.web;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class WebUtils {

    public static String PAGE_ROOT = "/Dashboard";

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getIP() {
        //获取主机IP地址
        String hostAddress = "";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return hostAddress;
    }
}
