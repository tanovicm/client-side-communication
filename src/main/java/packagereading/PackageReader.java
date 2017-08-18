package packagereading;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import typesofpackages.MessagePackage;
import utils.ByteUtils;

/**
 * The Package class is used for managing packages at client side.
 *
 * @author Marijana Tanovic
 */
public class PackageReader {

    /**
     * Map where are kept all types of packages which are in use in application.
     */
    private static HashMap<Integer, PackageFactory> registedFactories = new HashMap<Integer, PackageFactory>();

    /**
     * @param id
     *            Id of package (1 or 2).
     * @param typeOfPackage
     *            Type of package that should be put in map of registered factories..
     */

    public static void registerTypeOfPackage(final int id, final PackageFactory typeOfPackage) {
        registedFactories.put(id, typeOfPackage);
    }

    /**
     * Read bytes from socket and creates package based on read id.
     *
     * @param socket
     *            Socket from which are bytes read.
     * @return MessagePackage Created package based on read package id.
     * @throws IOException
     *             Exception if bytes can't be read. .
     */
    public MessagePackage read(final Socket socket) throws IOException {
        synchronized (socket) {
            System.out.println("Waiting for package..");
            final byte[] header = ByteUtils.readNBytesFromSocket(socket, 2 * Integer.BYTES);
            final int packageId = ByteUtils.getNthInteger(header, 0);
            final int packageLength = ByteUtils.getNthInteger(header, 1);
            System.out.println("Received heade (id: " + packageId + ", len: " + packageLength + ")");
            final byte[] body = ByteUtils.readNBytesFromSocket(socket, packageLength - header.length);
            return registedFactories.get(packageId).createPackage(header, body);
        }
    }
}
