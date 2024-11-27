import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class GetdataHandler implements HttpHandler {
	private HashMap<String, Integer> data;
    public GetdataHandler() {
        this.data = Main.map;
    }
	
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
        	data = Main.map;
        	String dataJson = mapToJson(data);
        	
            String response = String.format("""
                    {
                        "message": "Data fetched successfully",
                        "data": %s
                    }
                    """, dataJson);
        	
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
    private String mapToJson(HashMap<String, Integer> map) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        for (String key : map.keySet()) {
            Integer value = map.get(key);
            jsonBuilder.append("\"").append(key).append("\"")
                       .append(": ")
                       .append(value)
                       .append(", ");
        }

        if (jsonBuilder.length() > 1) {
            jsonBuilder.setLength(jsonBuilder.length() - 2);
        }

        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }
}
