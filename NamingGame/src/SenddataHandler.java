import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SenddataHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
        	String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            Map<String, Integer> data = parseJson(requestBody);

            int trial = data.getOrDefault("trial", 0);
            int agent = data.getOrDefault("agent", 0);
            int word = data.getOrDefault("word", 0);
            int lat = data.getOrDefault("lat", 0);
            System.out.println("Received data: " + data);
            System.out.print("Trials: " + trial + ", ");
            System.out.print("Agents: " + agent + ", ");
            System.out.println("Lat?: " + (lat == 1));
            
            String response = String.format("""
                {
                    "message": "Data fetched successfully",
                    "receivedData": {
                        "trial": %d,
                        "agent": %d,
                        "lat": %d,
                        "word": %d
                    }
                }
                """, trial, agent, lat, word);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            
            Main.trial(agent, 100000, (lat == 1), 2, false);
        } else {
            String response = "Method Not Allowed";
            exchange.sendResponseHeaders(405, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private Map<String, Integer> parseJson(String jsonString) {
        Map<String, Integer> result = new HashMap<>();

        jsonString = jsonString.trim().replace("{", "").replace("}", "").replace("\"", "");

        String[] pairs = jsonString.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                int value = Integer.parseInt(keyValue[1].trim());
                result.put(key, value);
            }
        }

        return result;
    }
}
