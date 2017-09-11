package utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * ByteUtils class is utility class for operating with bytes which are received and sent to socket.
 *
 * @author Marijana Tanovic
 */
public final class ByteUtils {

    /**
     * Utility classes should not have a public or default constructor.
     */
    private ByteUtils() {}

    /**
     * From array of bytes and given position, function calculates integer on that position in array of bytes.
     *
     * @param bytes
     *            Bytes from which integer should be read.
     * @param nthInteger
     *            Nth integer from array of bytes.
     * @return Nth integer from array of bytes.
     */
    public static int getNthInteger(final byte[] bytes, final int nthInteger) {
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.position(nthInteger * Integer.BYTES);
        return Integer.reverseBytes(buffer.getInt());
    }

    /**
     * Creates array of bytes from given array.
     *
     * @param fileds
     *            Variable array of integers.
     * @return Array of bytes from array of given integers.
     */
    public static byte[] createByteArray(final int... fileds) {
        final ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES * fileds.length);
        Arrays.stream(fileds).forEach(f -> buffer.putInt(Integer.reverseBytes(f)));
        return buffer.array();
    }

    /**
     * Read given number of bytes from socket.
     *
     * @param socket
     *            Socket from which read bytes.
     * @param numBytes
     *            Number of bytes to read.
     * @return Array with numBytes read.
     * @throws IOException
     *             Exception can raise reading from socket.
     */
    public static byte[] readNBytesFromSocket(final Socket socket, final int numBytes) throws IOException {
        final byte[] bytes = new byte[numBytes];
        new DataInputStream(socket.getInputStream()).readFully(bytes);
        return bytes;
    }

    /**
     * Writing array of bytes to socket.
     *
     * @param socket
     *            Socket where bytes should be written.
     * @param bytes
     *            Bytes that should be written.
     * @throws IOException
     *             Exception can raise while trying to write to socket if socket is closed or can't be written to.
     */
    public static void writeByteArrayToSocket(final Socket socket, final byte[] bytes) throws IOException {
        new DataOutputStream(socket.getOutputStream()).write(bytes);
    }
}
