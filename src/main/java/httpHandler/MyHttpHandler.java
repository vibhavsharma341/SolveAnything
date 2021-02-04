package httpHandler;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

import static httpHandler.SolveExpression.generateResponse;

public class MyHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String body = new String(httpExchange.getRequestBody().readAllBytes());
        String contentType = httpExchange.getRequestHeaders().getFirst("Content-Type");
        String reqMethod = httpExchange.getRequestMethod();
        if("GET".equals(reqMethod) && "application/json".equals(contentType)) {
            handleResponse(httpExchange, body);
        }else {
            handleNotFound(httpExchange);
        }
    }

    public static void handleNotFound(HttpExchange httpExchange) throws IOException {

        OutputStream outputStream = httpExchange.getResponseBody();

        String resp = "URL not found : " + httpExchange.getRequestURI().toString();

        httpExchange.sendResponseHeaders(404, resp.length());
        outputStream.write(resp.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private static void handleResponse(HttpExchange httpExchange, String body) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();

        // parse body and generate the value for the mathematical expression
        JSONObject resp = generateResponse(body, httpExchange.getRequestURI());

        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, resp.toJSONString().length());

        outputStream.write(resp.toJSONString().getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
