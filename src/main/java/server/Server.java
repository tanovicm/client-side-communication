package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import packages.DummyPackage;
import packages.MessagePackage;
import utils.ByteUtils;
import utils.PackageReader;

/**
 * The Server class for testing purposes.
 *
 * @author Marijana Tanovic
 *
 */
public final class Server {

    /**
     * Map of active packages.
     */
    public static final Map<Integer, DummyPackage> activePackages = Collections
            .synchronizedMap(new HashMap<Integer, DummyPackage>());
    /**
     * Id of package.
     */
    private static int id;

    /**
     * Utility classes should not have a public or default constructor.
     */
    private Server() {}

    /**
     * Each second, new array of bytes is sent to client with random delay.
     *
     * @param clientSocket
     *            Socket which receives message.
     */
    private static void sendPackages(final Socket clientSocket) {
        try {
            while (true) {
                final int delay = ThreadLocalRandom.current().nextInt(1, 21);
                final DummyPackage pack = new DummyPackage(ByteUtils.createByteArray(1, 16),
                        ByteUtils.createByteArray(id, delay));
                activePackages.put(new Integer(id), pack);
                id = id + 1;

                ByteUtils.writeByteArrayToSocket(clientSocket, pack.getHeader());
                ByteUtils.writeByteArrayToSocket(clientSocket, pack.getBody());
                System.out.println("Send: " + pack);
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function that receives packages from client socket.
     *
     * @param clientSocket
     *            Socket from which packages are read.
     */
    private static void receivePackages(final Socket clientSocket) {
        try {
            while (true) {
                final MessagePackage pack = PackageReader.read(clientSocket);
                System.out.println("Received: " + pack);

                final Integer idPacket = new Integer(ByteUtils.getNthInteger(pack.getBody(), 0));

                if (activePackages.get(idPacket) == null) {
                    System.out.println("[ERROR]: Package " + idPacket + " null");
                } else {
                    if (!activePackages.get(idPacket).expired()) {
                        System.out.println("[ERROR]: Package " + idPacket + " not expierd, time left "
                                + activePackages.get(idPacket).getDelay());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates server socket and trying to connect to client. For each sent and received package new thread is started.
     *
     * @param args
     *            Command line arguments.
     */
    public static void main(final String[] args) {
        try (ServerSocket server = new ServerSocket(4000)) {
            System.out.println("Listening to port 4000...");
            while (true) {
                final Socket clientSocket = server.accept();
                System.out.println("Client connected");
                new Thread(() -> sendPackages(clientSocket)).start();
                new Thread(() -> receivePackages(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
