package top.cyqi.utils;

import top.cyqi.GrasscuttersWebDashboard;

public class GCGMUtils {

    private static final Runtime RUNTIME = Runtime.getRuntime();

    public static String GetDispatchAddress() {
        return "ws" + (GrasscuttersWebDashboard.getServerConfig().getDispatchOptions().FrontHTTPS ? "s" : "") + "://" +
                (GrasscuttersWebDashboard.getServerConfig().getDispatchOptions().PublicIp.isEmpty() ? GrasscuttersWebDashboard.getServerConfig().getDispatchOptions().Ip : GrasscuttersWebDashboard.getServerConfig().getDispatchOptions().PublicIp) +
                ":" + (GrasscuttersWebDashboard.getServerConfig().getDispatchOptions().PublicPort != 0 ? GrasscuttersWebDashboard.getServerConfig().getDispatchOptions().PublicPort : GrasscuttersWebDashboard.getServerConfig().getDispatchOptions().Port);
    }

    public static long GetFreeJVMMemory() {
        return GCGMUtils.RUNTIME.freeMemory();
    }

    public static long GetAllocatedJVMMemory() {
        return GCGMUtils.RUNTIME.totalMemory();
    }
}
