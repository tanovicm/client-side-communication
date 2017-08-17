package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

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
	 * Id.
	 */
	public static int id = 0;
	/**
	 * Map.
	 */
	public static Map<Integer, DummyPackage> activePackages = Collections
			.synchronizedMap(new HashMap<Integer, DummyPackage>());

	/**
	 *
	 * @param args
	 *            Args.
	 */
	public static void main(final String[] args) {

		try (ServerSocket server = new ServerSocket(4000)) {
			System.err.println("Listening to port 4000 failed");
			final Socket clientSocket = server.accept();
			System.err.println("client connected");
			// Send packages
			Executors.newSingleThreadExecutor().execute(() -> {
				while (true) {
					try {
						final int delay = ThreadLocalRandom.current().nextInt(0, 21);
						final DummyPackage pack = new DummyPackage(ByteUtils.createByteArray(1, 8),
								ByteUtils.createByteArray(id++, delay));
						activePackages.put(new Integer(id), pack);
						ByteUtils.writeByteArrayToSocket(clientSocket, pack.getHeader());
						ByteUtils.writeByteArrayToSocket(clientSocket, pack.getBody());
						System.out.println("Send: " + pack);
						Thread.sleep(1000);
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			// Receive packages
			Executors.newSingleThreadExecutor().execute(() -> {
				while (true) {
					try {
						final MessagePackage pack = new PackageReader().read(clientSocket);
						System.out.println("Received: " + pack);

						final Integer idPacket = new Integer(ByteUtils.getNthInteger(pack.getBody(), 0));

						if (activePackages.get(idPacket).expired()) {
							System.out.println("[ERROR]: Package not expierd");
						}
						activePackages.remove(idPacket);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
