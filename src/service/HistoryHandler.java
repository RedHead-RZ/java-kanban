package service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        getRequestParams(exchange);
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")
                && exchange.getRequestURI().getPath().equals("/history")) {
            sendText(exchange, gson.toJson(manager.getHistory()), 200);
        } else {
            sendUnavailableMethod(exchange);
        }
    }
}
