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
	 * Package to handle.
	 */
	private MessagePackage pack;

	/**
	 * Socket.
	 */
	private Socket socket;

	/**
	 *
	 * @param pack
	 *            Package.
	 * @param socket
	 *            Socket.
	 */
	public PackageHandler(final MessagePackage pack, final Socket socket) {
		this.pack = pack;
		this.socket = socket;
	}

	/**
	 * Run.
	 */
	public void run() {
		try {
			pack.send(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
