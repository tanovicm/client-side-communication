package packagehandlers;

import java.io.IOException;
import java.net.Socket;

import typesofpackages.MessagePackage;

/**
 * The PackageHandler class is used to handle incoming packages.
 *
 * @author Marijana Tanovic
 */
public class PackageHandler extends Thread {

    /**
     * Package to be handled.
     */
    private MessagePackage pack;

    /**
     * Client socket.
     */
    private Socket socket;

    /**
     * Constructor.
     *
     * @param pack
     *            Package gotten from socket.
     * @param socket
     *            Socket connection.
     */
    public PackageHandler(final MessagePackage pack, final Socket socket) {
        this.pack = pack;
        this.socket = socket;
    }

    /**
     * Runs thread to send handled package.
     */
    public void run() {
        try {
            pack.send(socket);
            System.out.println("Send: " + pack);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
