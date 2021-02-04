import com.sun.net.httpserver.HttpServer;
import httpHandler.MyHttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class RunServer {

    public static final Logger logger = Logger.getLogger("MyLogger");

    public static void main(String[] args) throws IOException {

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        server.createContext("/evaluate", new MyHttpHandler());
        server.createContext("/details", new MyHttpHandler());
        server.setExecutor(threadPoolExecutor);
        server.start();
        logger.info(" Server started on port 8001");
    }
}
