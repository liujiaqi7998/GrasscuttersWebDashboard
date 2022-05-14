package top.cyqi.PluginCommand;

import emu.grasscutter.command.Command;
import emu.grasscutter.command.CommandHandler;
import emu.grasscutter.game.mail.Mail;
import emu.grasscutter.game.player.Player;
import top.cyqi.GrasscuttersWebDashboard;
import top.cyqi.utils.Utils;
import top.cyqi.utils.WebUtils;
import top.cyqi.websocket.ServerUtils;
import top.cyqi.websocket.json.PlayerWebKey;

import java.util.List;
import java.util.Map;

@Command(label = "webtools",
        usage = "webtools",
        description = "发送 webtools 验证码邮件",
        aliases = {"webt", "wt"},
        permission = "GrasscuttersWebDashboard.webtools")
public class webtoolsPluginCommand implements CommandHandler {

    public void execute(Player sender, Player targetPlayer, List<String> args) {
        if (targetPlayer == null) {
            CommandHandler.sendMessage(sender, "[WEB控制台] 错误: 玩家UID不能为空");
            return;
        }
        //判断玩家是否在线
        Map<Integer, Player> playerMap = GrasscuttersWebDashboard.getGameServer().getPlayers();
        if (!playerMap.containsKey(targetPlayer.getUid())) {
            CommandHandler.sendMessage(sender, "[WEB控制台] 错误: 玩家UID不在线");
            return;
        }
        //随机8位数字
        String random = Utils.generate8UID();
        while (ServerUtils.tempPlayersData.containsKey(random)) {
            //判断密钥是否超过key_timeout分钟
            if (System.currentTimeMillis() - ServerUtils.tempPlayersData.get(random).getTime > GrasscuttersWebDashboard.key_timeout) {
                ServerUtils.tempPlayersData.remove(random);
            } else {
                random = Utils.generate8UID();
            }
        }

        PlayerWebKey playerWebKey = new PlayerWebKey();
        playerWebKey.Key = random;
        playerWebKey.player = targetPlayer;
        playerWebKey.getTime = System.currentTimeMillis();
        ServerUtils.tempPlayersData.put(random, playerWebKey);
        Mail mail = new Mail();
        mail.mailContent.title = "登录网页管理工具验证码";
        mail.mailContent.sender = "网页管理工具";

        String ServerUrl = Utils.GetDispatchAddress() + WebUtils.WebToolsPAGE_ROOT;
        String Url = WebUtils.WebtoolsURL + "?key=" + random + "&server=" + ServerUrl;
        mail.mailContent.content = "您的验证码是: " + random + "\n\n" + "请点击以下链接打开: \n"
                + "<type=\"webview\" text=\"游戏内打开网页工具箱\" href=\"" + Url + "\"/>"
                + "<type=\"browser\" text=\"浏览器打开网页工具箱\" href=\"" + Url + "\"/>";
        targetPlayer.sendMail(mail);
        CommandHandler.sendMessage(sender, "[WEB控制台] 给 [" + targetPlayer.getNickname() + "] 发送WebTools验证码邮件成功！");
    }

}