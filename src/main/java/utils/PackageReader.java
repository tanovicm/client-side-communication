package utils;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import packagefactories.CancelPackageFactory;
import packagefactories.DummyPackageFactory;
import packagefactories.PackageFactory;
import packages.MessagePackage;

/**
 * The PackageReader class is used for reading bytes from socket.
 *
 * <p>
 * Read bytes from socket and based on read bytes create appropriate package.
 * </p>
 *
 * @author Marijana Tanovic
 */
public final class PackageReader {

    /**
     * Map where are kept all types of packages which are in use in application.
     */
    private static HashMap<Integer, PackageFactory> registedFactories = new HashMap<Integer, PackageFactory>();

    // Add types of packages in map of registered factories.
    static {
        registedFactories.put(1, new DummyPackageFactory());
        registedFactories.put(2, new CancelPackageFactory());
    }

    /**
     * Utility classes should not have a public or default constructor.
     */
    private PackageReader() {}

    /**
     * Read bytes from socket and creates package based on read id.
     *
     * @param socket
     *            Socket from which are bytes read.
     * @return MessagePackage Created package based on read package id.
     * @throws IOException
     *             Thrown by readNBytesFromSocket function when reading from socket.
     */
    public static MessagePackage read(final Socket socket) throws IOException {
        synchronized (socket) {
            final byte[] header = ByteUtils.readNBytesFromSocket(socket, 2 * Integer.BYTES);
            final int packageId = ByteUtils.getNthInteger(header, 0);
            final int packageLength = ByteUtils.getNthInteger(header, 1);
            final byte[] body = ByteUtils.readNBytesFromSocket(socket, packageLength - header.length);
            return registedFactories.get(packageId).createPackage(header, body);
        }
    }
}
