package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import packagereading.CancelPackageFactory;
import packagereading.DummyPackageFactory;
import packagereading.PackageReader;
import typesofpackages.DummyPackage;
import typesofpackages.MessagePackage;
import utils.ByteUtils;

/**
 * The Server class for test.
 *
 * @author Marijana Tanovic
 *
 */
public class Server {

    /**
     * Map.
     */
    public static final Map<Integer, DummyPackage> activePackages = Collections
            .synchronizedMap(new HashMap<Integer, DummyPackage>());
    /**
     * Id.
     */
    private static int id;

    /**
     * Registers all types of packages from server.
     */
    public static void registryAllTypesOfPackages() {
        final int dummyPackageId = 1;
        final int cancelPackageId = 2;
        PackageReader.registerTypeOfPackage(dummyPackageId, new DummyPackageFactory());
        PackageReader.registerTypeOfPackage(cancelPackageId, new CancelPackageFactory());

    }

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

    private static void receivePackages(final Socket clientSocket) {
        try {
            registryAllTypesOfPackages();
            while (true) {
                final MessagePackage pack = new PackageReader().read(clientSocket);
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
     *
     * @param args
     *            Args.
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
