package top.cyqi.handlers;

import ch.qos.logback.core.read.ListAppender;
import top.cyqi.websocket.WebSocketServer;
import top.cyqi.websocket.json.WSData;

import java.util.ArrayList;

public class WebConsoleListAppender<ILoggingEvent> extends ListAppender<ILoggingEvent> {
    public ArrayList<ILoggingEvent> list = new ArrayList<>();

    public WebConsoleListAppender() {
    }

    protected void append(ILoggingEvent e) {
        WebSocketServer.ClitenContextMap.keySet().stream().filter(ctx -> ctx.session.isOpen()).forEach(session -> session.send(new WSData("cmd_msg",e.toString())));
        this.list.add(e);
    }
}
