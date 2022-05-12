package top.cyqi.handlers;

import emu.grasscutter.game.player.Player;
import emu.grasscutter.utils.MessageHandler;
import io.javalin.websocket.WsMessageContext;
import top.cyqi.websocket.json.WSData;

public class WebToolsMessageHandler extends MessageHandler {

    public WsMessageContext wsMessageContext;
    public Player player;

    public void append(String message){
        wsMessageContext.send(new WSData("success", message));
        player.setMessageHandler(null);
    }

}
