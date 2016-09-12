package portforwarder;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.JHTTP;

public class PFHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(
            JHTTP.class.getCanonicalName());
    
    private final List<String> servers;
    private final Random randomGenerator;

    public PFHandler(List<String> servers) {
        randomGenerator = new Random();

        this.servers = servers;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = "";
        int serverCount = servers.size();

        int index = randomGenerator.nextInt(serverCount);
        String server = servers.get(index);
        
        try {
            URL url = new URL("http://" + server + t.getRequestURI().getPath());
            LOGGER.log(Level.INFO, "using server: http://{0}", server);
            
            response += "<body bgcolor = 'blue'>" + server + "</body>\n";
            LOGGER.log(Level.INFO, "Context: {0} remote adress: {1}", new Object[]{t.getHttpContext().getPath(), t.getRequestURI().getPath()});
            
            URLConnection yc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                response += inputLine + "\n";
            }
            
            in.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error{0}", e);
        }
        
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
