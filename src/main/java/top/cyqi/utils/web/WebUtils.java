package top.cyqi.utils.web;


import emu.grasscutter.utils.Utils;
import express.Express;
import io.javalin.http.staticfiles.Location;
import top.cyqi.GrasscuttersWebDashboard;

import java.io.File;

public class WebUtils {

    public static final String PAGE_ROOT = "/gm";

    public static void addStaticFiles(File staticRoot) {
        Express app = GrasscuttersWebDashboard.getDispatchServer().getServer();
        app.raw().config.precompressStaticFiles = false; // MUST BE SET TO FALSE OR FILES SUCH AS IMAGES WILL APPEAR CORRUPTED
        app.raw().config.addStaticFiles(PAGE_ROOT, staticRoot.getAbsolutePath(), Location.EXTERNAL);
        app.raw().config.addSinglePageRoot(PAGE_ROOT, Utils.toFilePath(staticRoot.getPath() + "/index.html"), Location.EXTERNAL);
    }
}
