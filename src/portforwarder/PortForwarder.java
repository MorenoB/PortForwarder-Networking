package portforwarder;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.JHTTP;
import server.RequestProcessor;

public class PortForwarder {

    private static final Logger LOGGER = Logger.getLogger(
            JHTTP.class.getCanonicalName());
    private static final int NUM_THREADS = 50;

    private File rootDirectory;
    private final int port = 80;
    private static final String INDEX_FILE = "index.html";
    private List serverPorts;

    public PortForwarder(File rootDirectory/*, ArrayList serverPorts*/) throws IOException {
        if (!rootDirectory.isDirectory()) {
            throw new IOException(rootDirectory
                    + " does not exist as a directory");
        }
        this.rootDirectory = rootDirectory;
    }

    public void start() throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
        try (ServerSocket server = new ServerSocket(port)) {
            LOGGER.log(Level.INFO, "Accepting connections on port {0}", server.getLocalPort());
            LOGGER.log(Level.INFO, "Document Root: {0}", rootDirectory);

            while (true) {
                try {
                    Socket request = server.accept();
                    try {
                        request = new Socket("localhost", 50);
                        LOGGER.log(Level.INFO, "Connected client to port {0}", request.getPort());
                    } catch (IOException ex) {
                        LOGGER.log(Level.WARNING, "Could not find a suitable server on port {0} | {1}", new Object[]{ 50, ex } );
                    }
                    Runnable r = new RequestProcessor(
                            rootDirectory, INDEX_FILE, request);
                    pool.submit(r);
                } catch (IOException ex) {
                    LOGGER.log(Level.WARNING, "Error accepting connection", ex);
                }
            }
        }
    }
}
