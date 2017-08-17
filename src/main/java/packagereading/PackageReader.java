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
	 * Number indicating type of package.
	 */
	private static HashMap<Integer, PackageFactory> registedFactories = new HashMap<Integer, PackageFactory>();

	/**
	 * @param id
	 *            Id of package.
	 * @param pack
	 *            Package to be registered.
	 */

	public static void registerTypeOfPackage(final int id, final PackageFactory pack) {
		registedFactories.put(id, pack);
	}

	/**
	 * @param socket
	 *            Socket from which bytes are read.
	 * @return MessagePackage
	 * @throws IOException
	 *             Onako.
	 */
	public MessagePackage read(final Socket socket) throws IOException {
		final byte[] header = ByteUtils.readNBytesFromSocket(socket, 2 * Integer.BYTES);
		final int packageId = ByteUtils.getNthInteger(header, 1);
		final int packageLength = ByteUtils.getNthInteger(header, 1);
		final byte[] body = ByteUtils.readNBytesFromSocket(socket, packageLength);
		return registedFactories.get(packageId).readPackage(header, body);
	}
}
