package Main;

import server.JHTTP;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import portforwarder.PortForwarder;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(
            JHTTP.class.getCanonicalName());

    public static void main(String[] args) throws IOException {

        /* if(args.length != 2 && args.length != 3)
        {
            System.out.println("Usage: java -jar PortForwarder.jar [S/P] port docroot (only if the flag S is chosen)");
            return;
        }*/
        File docroot;

        try {
            docroot = new File(args[2]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Usage: java -jar PortForwarder.jar [S/P] port docroot (only if the flag S is chosen)");
            return;
        }

        if ("P".equals(args[0])) {
            PortForwarder pf = new PortForwarder(docroot);
            try {
                pf.start();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Server could not start", ex);
            }
            //Start port forwarder
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

        System.out.println("Usage: java -jar PortForwarder.jar [S/P] port docroot (only if the flag S is chosen)");

    }
}
