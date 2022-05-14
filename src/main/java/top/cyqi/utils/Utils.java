package top.cyqi.utils;

import top.cyqi.GrasscuttersWebDashboard;

import java.util.Random;

public class Utils {

    private static final Runtime RUNTIME = Runtime.getRuntime();

    public static String GetDispatchAddress() {

        return "ws" + (GrasscuttersWebDashboard.getServerConfig().server.dispatch.encryption.useEncryption ? "s" : "") + "://" +
                (GrasscuttersWebDashboard.getServerConfig().server.dispatch.accessAddress.isEmpty() ? GrasscuttersWebDashboard.getServerConfig().server.dispatch.bindAddress : GrasscuttersWebDashboard.getServerConfig().server.dispatch.accessAddress) +
                ":" + (GrasscuttersWebDashboard.getServerConfig().server.dispatch.accessPort != 0 ? GrasscuttersWebDashboard.getServerConfig().server.dispatch.accessPort : GrasscuttersWebDashboard.getServerConfig().server.dispatch.bindPort);
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
