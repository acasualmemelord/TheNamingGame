import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/get", new GetdataHandler()).getFilters().add(new CorsFilter());
        server.createContext("/send", new SenddataHandler()).getFilters().add(new CorsFilter());
        server.setExecutor(null); 
        System.out.println("Server is running on http://localhost:8080");
        server.start();
        Main.trial(20, 10000, true, 2, false);
    }
}