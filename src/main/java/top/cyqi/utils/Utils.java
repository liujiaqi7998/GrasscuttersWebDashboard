package top.cyqi.utils;

import top.cyqi.GrasscuttersWebDashboard;

import java.util.Random;

public class Utils {

    private static final Runtime RUNTIME = Runtime.getRuntime();

    public static String GetDispatchAddress() {

        return "ws" + (GrasscuttersWebDashboard.getServerConfig().server.http.encryption.useEncryption ? "s" : "") + "://" +
                (GrasscuttersWebDashboard.getServerConfig().server.http.accessAddress.isEmpty() ? GrasscuttersWebDashboard.getServerConfig().server.http.bindAddress : GrasscuttersWebDashboard.getServerConfig().server.http.accessAddress) +
                ":" + (GrasscuttersWebDashboard.getServerConfig().server.http.accessPort != 0 ? GrasscuttersWebDashboard.getServerConfig().server.http.accessPort : GrasscuttersWebDashboard.getServerConfig().server.http.bindPort);
    }

    public static long GetFreeJVMMemory() {
        return Utils.RUNTIME.freeMemory();
    }

    public static long GetAllocatedJVMMemory() {
        return Utils.RUNTIME.totalMemory();
    }

    /***
     * 生成uid 8位数字
     */
    public static String generate8UID(){
        Random random = new Random();
        String result="";
        for(int i=0;i<8;i++){
            //首字母不能为0
            result += (random.nextInt(9)+1);
        }
        return result;
    }
}
