package top.cyqi.utils;

import emu.grasscutter.Grasscutter;
import top.cyqi.GrasscuttersWebDashboard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class GCGMUtils {

    private static final Runtime RUNTIME = Runtime.getRuntime();

    public static String GetDispatchAddress() {
        return "http" + (Grasscutter.getConfig().getDispatchOptions().FrontHTTPS ? "s" : "") + "://" +
                (Grasscutter.getConfig().getDispatchOptions().PublicIp.isEmpty() ? Grasscutter.getConfig().getDispatchOptions().Ip : Grasscutter.getConfig().getDispatchOptions().PublicIp) +
                ":" + (Grasscutter.getConfig().getDispatchOptions().PublicPort != 0 ? Grasscutter.getConfig().getDispatchOptions().PublicPort : Grasscutter.getConfig().getDispatchOptions().Port);
    }

    public static boolean CopyFile(String resourceName, String copyLocation) {
        try {
            Grasscutter.getLogger().info("[WEB控制台] 复制文件 'DefaultWebApp.zip' to './plugins/GCGM'");
            Files.copy(GrasscuttersWebDashboard.getInstance().getResource(resourceName), Paths.get(new File(copyLocation).toURI()), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            Grasscutter.getLogger().error(String.format("[WEB控制台] 进行复制时出错'%s' 到 '%s'", resourceName, copyLocation));
            e.printStackTrace();
            return false;
        }
    }

    public static long GetFreeJVMMemory() {
        return GCGMUtils.RUNTIME.freeMemory();
    }

    public static long GetAllocatedJVMMemory() {
        return GCGMUtils.RUNTIME.totalMemory();
    }
}
