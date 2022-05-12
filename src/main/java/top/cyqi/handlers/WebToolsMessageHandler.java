package top.cyqi.handlers;

import emu.grasscutter.utils.MessageHandler;
import io.javalin.websocket.WsMessageContext;
import top.cyqi.websocket.json.WSData;

public class WebToolsMessageHandler extends MessageHandler {

    public WsMessageContext wsMessageContext;

    public void append(String message){
        String msg = getMessage();
        wsMessageContext.send(new WSData("success", message));
        msg += message + "\r\n\r\n";
        setMessage(msg);
    }

}
