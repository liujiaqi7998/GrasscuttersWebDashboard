package top.cyqi.utils.web;

import emu.grasscutter.utils.MessageHandler;
import io.javalin.websocket.WsMessageContext;
import top.cyqi.websocket.json.WSData;

public class WebToolsMessageHandler extends MessageHandler {

    public WsMessageContext wsMessageContext;

    private final String message;

    public WebToolsMessageHandler(){
        this.message = "";
    }


    public String getMessage(){
        wsMessageContext.send(new WSData("success", message));
        return this.message;
    }

    public void setMessage(String message) {
        wsMessageContext.send(new WSData("success", message));
    }
}
