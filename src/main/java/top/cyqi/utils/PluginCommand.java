package top.cyqi.utils;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.command.Command;
import emu.grasscutter.command.CommandHandler;
import emu.grasscutter.game.mail.Mail;
import emu.grasscutter.game.player.Player;
import top.cyqi.GrasscuttersWebDashboard;
import top.cyqi.utils.web.WebUtils;
import top.cyqi.websocket.ServerUtils;
import top.cyqi.websocket.json.PlayerWebKey;
import top.cyqi.websocket.json.WSData;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Command(label = "webtools",
        usage = "webtools",
        description = "发送 webtools 验证码邮件",
        aliases = {"webtools"},
        permission = "webtools.console")
public class PluginCommand implements CommandHandler {

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
        String random = Long.toString((long) (Math.random() * 100000000));
        while (ServerUtils.tempPlayersData.containsKey(random)) {
            //判断密钥是否超过key_timeout分钟
            if (System.currentTimeMillis() - ServerUtils.tempPlayersData.get(random).getTime > GrasscuttersWebDashboard.key_timeout) {
                ServerUtils.tempPlayersData.remove(random);
            } else {
                random = Long.toString((long) (Math.random() * 100000000));
            }
        }

        //定时器key_timeout分钟后执行删除
        String finalRandom = random;
        new Timer("Auto-delete-" + random).schedule(new TimerTask() {
            @Override
            public void run() {
                ServerUtils.tempPlayersData.remove(finalRandom);
            }
        }, GrasscuttersWebDashboard.key_timeout);


        PlayerWebKey playerWebKey = new PlayerWebKey();
        playerWebKey.Key = random;
        playerWebKey.player = targetPlayer;
        playerWebKey.getTime = System.currentTimeMillis();
        ServerUtils.tempPlayersData.put(random, playerWebKey);
        Mail mail = new Mail();
        mail.mailContent.title = "登录网页管理工具验证码";
        mail.mailContent.sender = "网页管理工具";
        String Url = WebUtils.WebtoolsURL + "?key=" + random + "&server=" + GCGMUtils.GetDispatchAddress() + WebUtils.WebToolsPAGE_ROOT;
        //编码URL
        mail.mailContent.content = "您的验证码是: " + random + "\n\n" + "，请点击以下链接打开: \n" + "<type=\"browser\" text=\"打开网页工具箱\" href=\"" + Url + "\"/>";
        targetPlayer.sendMail(mail);
        CommandHandler.sendMessage(sender, "[WEB控制台] 给 [" + targetPlayer.getNickname() + "] 发送WebTools验证码邮件成功！");
    }

}