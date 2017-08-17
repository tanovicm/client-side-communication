package utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Utillity class.
 *
 * @author Marijana
 *
 */
public class ByteUtils {
	/**
	 *
	 * @param bytes
	 *            Bytes
	 * @param nthInt
	 *            Nth int.
	 * @return Int.
	 */
	public static int getNthInteger(final byte[] bytes, final int nthInt) {
		return ByteBuffer.wrap(bytes).getInt();
		// TODO DA LI MOZE OVAKO?
	}

	/**
	 *
	 * @param fileds
	 *            Variable array of ints.
	 * @return bytes.
	 */
	public static byte[] createByteArray(final int... fileds) {
		final ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES * fileds.length);
		buffer.asIntBuffer().put(fileds);
		return buffer.array();
	}

	/**
	 *
	 * @param socket
	 *            Socket.
	 * @param nthBytes
	 *            N bytes.
	 * @return Array of bytes.
	 * @throws IOException
	 *             Exception.
	 */
	public static byte[] readNBytesFromSocket(final Socket socket, final int nthBytes) throws IOException {
		final byte[] bytes = new byte[nthBytes];
		new DataInputStream(socket.getInputStream()).readFully(bytes);
		return bytes;
	}

	/**
	 *
	 * @param socket
	 *            Socket.
	 * @param bytes
	 *            Bytes.
	 * @throws IOException
	 *             Exception.
	 */
	public static void writeByteArrayToSocket(final Socket socket, final byte[] bytes) throws IOException {
		new DataOutputStream(socket.getOutputStream()).write(bytes);
	}

}