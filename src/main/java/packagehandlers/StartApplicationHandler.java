package packagehandlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

import typesofpackages.CancelPackage;
import typesofpackages.DummyPackage;
import typesofpackages.MessagePackage;

/**
 * The StartApplicationHandler class is used to handle starting application.
 *
 * @author Marijana Tanovic
 */
public class StartApplicationHandler extends Thread {
	/**
	 * File.
	 */
	private File fileName;
	/**
	 * Socket.
	 */
	private Socket socket;

	/**
	 *
	 * @param fileName
	 *            FIle.
	 * @param socket
	 *            Socket.
	 */
	public StartApplicationHandler(final File fileName, final Socket socket) {
		this.fileName = fileName;
		this.socket = socket;

	}

	/**
	 * RUn.
	 */
	@SuppressWarnings("unchecked")

	public void run() {
		System.out.println(fileName.length());

		try (FileInputStream fis = new FileInputStream(fileName)) {
			final ObjectInputStream ois = new ObjectInputStream(fis);

			final List<DummyPackage> packages = (List<DummyPackage>) ois.readObject();
			packages.forEach(pack -> {
				final MessagePackage p = pack.expired() ? new CancelPackage(pack) : pack;
				new PackageHandler(p, socket).start();
			});
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
