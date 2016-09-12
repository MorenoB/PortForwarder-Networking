package portforwarder;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.JHTTP;

public class PortForwarder {

    private static final Logger LOGGER = Logger.getLogger(
            JHTTP.class.getCanonicalName());

    private int port = 80;    
    private final List<String> serverList;
    
    private HttpServer server;
    private final PFHandler customHandler;

    public PortForwarder(int port, List<String> serverList) throws IOException {
        
        this.port = port;
        this.serverList = serverList;
        
        customHandler = new PFHandler(serverList);
    }

    public void Start() throws IOException {
        
        this.server = HttpServer.create(new InetSocketAddress(this.port), 0);
        
        this.server.createContext("/", customHandler);
        this.server.setExecutor(null);
        
        this.server.start();
        LOGGER.log(Level.INFO, "Server succesfully started.");
    }
}
