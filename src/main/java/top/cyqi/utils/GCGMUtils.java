package top.cyqi.utils;

import emu.grasscutter.Grasscutter;

public class GCGMUtils {

    private static final Runtime RUNTIME = Runtime.getRuntime();

    public static String GetDispatchAddress() {
        return "ws" + (Grasscutter.getConfig().getDispatchOptions().FrontHTTPS ? "s" : "") + "://" +
                (Grasscutter.getConfig().getDispatchOptions().PublicIp.isEmpty() ? Grasscutter.getConfig().getDispatchOptions().Ip : Grasscutter.getConfig().getDispatchOptions().PublicIp) +
                ":" + (Grasscutter.getConfig().getDispatchOptions().PublicPort != 0 ? Grasscutter.getConfig().getDispatchOptions().PublicPort : Grasscutter.getConfig().getDispatchOptions().Port);
    }

    public static long GetFreeJVMMemory() {
        return GCGMUtils.RUNTIME.freeMemory();
    }

    public static long GetAllocatedJVMMemory() {
        return GCGMUtils.RUNTIME.totalMemory();
    }
}
