package top.cyqi.utils;

import top.cyqi.GrasscuttersWebDashboard;

public class GCGMUtils {

    private static final Runtime RUNTIME = Runtime.getRuntime();

    public static String GetDispatchAddress() {

        return "ws" + (GrasscuttersWebDashboard.getServerConfig().server.dispatch.encryption.useEncryption ? "s" : "") + "://" +
                (GrasscuttersWebDashboard.getServerConfig().server.dispatch.accessAddress.isEmpty() ? GrasscuttersWebDashboard.getServerConfig().server.dispatch.bindAddress : GrasscuttersWebDashboard.getServerConfig().server.dispatch.accessAddress) +
                ":" + (GrasscuttersWebDashboard.getServerConfig().server.dispatch.accessPort != 0 ? GrasscuttersWebDashboard.getServerConfig().server.dispatch.accessPort : GrasscuttersWebDashboard.getServerConfig().server.dispatch.bindPort);
    }

    public static long GetFreeJVMMemory() {
        return GCGMUtils.RUNTIME.freeMemory();
    }

    public static long GetAllocatedJVMMemory() {
        return GCGMUtils.RUNTIME.totalMemory();
    }
}
