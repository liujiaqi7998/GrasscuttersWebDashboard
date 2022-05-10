package top.cyqi.utils.web;


import emu.grasscutter.utils.Utils;
import express.Express;
import io.javalin.http.staticfiles.Location;
import top.cyqi.GrasscuttersWebDashboard;
import java.io.File;

public class WebUtils {

    public static final String PAGE_ROOT = "/Dashboard";

    public static void addStaticFiles(File staticRoot) {
        Express app = GrasscuttersWebDashboard.getDispatchServer().getServer();
        app.raw().config.precompressStaticFiles = false; //必须设置为 FALSE，否则图像等文件将出现损坏
        app.raw().config.addStaticFiles(PAGE_ROOT, staticRoot.getAbsolutePath(), Location.EXTERNAL);
        app.raw().config.addSinglePageRoot(PAGE_ROOT, Utils.toFilePath(staticRoot.getPath() + "/index.html"), Location.EXTERNAL);
    }
}
