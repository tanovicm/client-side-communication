package client;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import packagehandlers.ExitApplicationHandler;
import packagehandlers.PackageHandler;
import packagehandlers.StartApplicationHandler;
import packagereading.CancelPackageFactory;
import packagereading.DummyPackageFactory;
import packagereading.PackageReader;
import typesofpackages.MessagePackage;

/**
 * The Client class is used communicate with server.
 *
 * @author Marijana Tanovic
 */
public final class Client {
    /**
     * List of all active packages in application while running.
     */
    public static List<MessagePackage> activePackages = Collections.synchronizedList(new ArrayList<MessagePackage>());

    /**
     * Connection address.
     */
    private static String connectionIpAddress;

    /**
     * Connection port.
     */
    private static int connectionPort;

    /**
     * Filename, where are stored packages which couldn't be sent.
     */
    private static File fileName;

    /**
     * Utility classes should not have a public or default constructor.
     */
    private Client() {}

    /**
     * Registers all types of packages which could be received from server.
     */
    public static void registryAllTypesOfPackages() {
        final int dummyPackageId = 1;
        final int cancelPackageId = 2;
        PackageReader.registerTypeOfPackage(dummyPackageId, new DummyPackageFactory());
        PackageReader.registerTypeOfPackage(cancelPackageId, new CancelPackageFactory());

    }

    /**
     * General settings for application.
     *
     * @param args
     *            Arguments for running application.
     */
    public static void parseArgs(final String[] args) {
        if (args.length != 2) {
            System.out.println("Proper Usage is: java program adress port");
            System.exit(0);
        }

        connectionIpAddress = args[0];
        connectionPort = Integer.parseInt(args[1]);
        fileName = new File("/home/marijana/workspace/Untitled Folder/help.txt");

        System.out.println("ADRESA: " + connectionIpAddress);
        System.out.println("Port: " + connectionPort);
    }

    /**
     *
     * @param args
     *            Parameters for application.
     */
    public static void main(final String[] args) {
        parseArgs(args);

        Runtime.getRuntime().addShutdownHook(new ExitApplicationHandler(fileName));

        try (Socket socket = new Socket(connectionIpAddress, connectionPort)) {
            new StartApplicationHandler(fileName, socket).start();

            registryAllTypesOfPackages();
            while (true) {
                final MessagePackage pack = new PackageReader().read(socket);
                System.out.println("Received: " + pack);
                new PackageHandler(pack, socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
