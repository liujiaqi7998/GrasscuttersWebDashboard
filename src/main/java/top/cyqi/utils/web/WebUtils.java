package top.cyqi.utils.web;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class WebUtils {

    public static String PAGE_ROOT = "/Dashboard";
    public static String WebToolsPAGE_ROOT = "/WebTools";
    public static String WebtoolsURL;
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

}
