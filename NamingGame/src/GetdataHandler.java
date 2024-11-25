import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class GetdataHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
    	HashMap<String, Integer> map = Main.map;
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
        	String temp = "";
        	for (String str : map.keySet()) {
        		temp += String.format("\"%s\": %d,\n", str, map.get(str));
        	}
        	if(temp.length() > 0) temp = temp.substring(0, temp.length() - 1);
        	String response = String.format("""
                    {
                        "message": "Data fetched successfully",
                        "data": [
                            %s
                        ]
                    }
                    """, temp.toString());

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else {
            String response = "Method Not Allowed";
            exchange.sendResponseHeaders(405, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
