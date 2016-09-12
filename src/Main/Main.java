package Main;

import server.JHTTP;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import portforwarder.PortForwarder;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(
            JHTTP.class.getCanonicalName());

    public static void main(String[] args) throws IOException {

        if ("P".equals(args[0])) {
            
            List<String> serverList = new ArrayList<>();
            
            int port = Integer.parseInt(args[1]);
            
            for (int i = 0; i < args.length; i++) {
                
                //Ignore the first and second argument
                //index 0 : P or S.
                //index 1 : port that will be used.
                if(i == 0 || i == 1) continue;
                
                String server = args[i];
                
                serverList.add(server);
            }
            
            PortForwarder pf = new PortForwarder(port,serverList);
            
            try {
                //Start port forwarder
                pf.Start();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Server could not start", ex);
            }
            return;
        }
        
        File docroot;

        try {
            docroot = new File(args[2]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            LOGGER.log(Level.WARNING, "Usage: java -jar PortForwarder.jar [S/P] port docroot (only if the flag S is chosen)");
            return;
        }

        if ("S".equals(args[0])) {
            //Start the server

            // set the port to listen on
            int port;

            try {
                port = Integer.parseInt(args[1]);
                if (port < 0 || port > 65535) {
                    port = 80;
                }
            } catch (RuntimeException ex) {
                port = 80;
            }

            try {
                JHTTP webserver = new JHTTP(docroot, port);
                webserver.start();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Server could not start", ex);
            }

            return;
        }

        LOGGER.log(Level.INFO, "Usage: java -jar PortForwarder.jar [S/P] port docroot (only if the flag S is chosen)");

    }
}
