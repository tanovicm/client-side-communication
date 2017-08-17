package client;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
public class Client {
	/**
	 * List of all active packages in application.
	 */
	public static List<MessagePackage> activePackages = new ArrayList<MessagePackage>();
	/**
	 * Connection address.
	 */
	private static String connectionIpAddress;
	/**
	 * Connection port.
	 */
	private static int connectionPort;
	/**
	 * Filename.
	 */
	private static File fileName;

	/**
	 *
	 * @return Address of connection.
	 */
	public static String getConnectionIpAddress() {
		return connectionIpAddress;
	}

	/**
	 *
	 * @param connectionIpAddress
	 *            Address of connection.
	 */
	public static void setConnectionIpAddress(final String connectionIpAddress) {
		Client.connectionIpAddress = connectionIpAddress;
	}

	/**
	 *
	 * @return Port.
	 */
	public static int getConnectionPort() {
		return connectionPort;
	}

	/**
	 *
	 * @param connectionPort
	 *            Port.
	 */
	public static void setConnectionPort(final int connectionPort) {
		Client.connectionPort = connectionPort;
	}

	/**
	 *
	 * @return File Name.
	 */
	public static File getFileName() {
		return fileName;
	}

	/**
	 *
	 * @param fileName
	 *            Name of file.
	 */
	public static void setFileName(final File fileName) {
		Client.fileName = fileName;
	}

	/**
	 * Registers all types of packages from server.
	 */
	static void registryAllTypesOfPackages() {
		final int dummyPackageId = 1;
		final int cancelPackageId = 2;
		PackageReader.registerTypeOfPackage(dummyPackageId, new DummyPackageFactory());
		PackageReader.registerTypeOfPackage(cancelPackageId, new CancelPackageFactory());

	}

	/**
	 *
	 * @param args
	 *            Parameters for application.
	 */
	public static void main(final String[] args) {
		Runtime.getRuntime().addShutdownHook(new ExitApplicationHandler(fileName));

		if (args.length != 2) {
			System.out.println("Proper Usage is: java program ipAdress port");
			System.exit(0);
		}
		setConnectionIpAddress(args[0]);
		setConnectionPort(Integer.parseInt(args[1]));
		setFileName(new File("/home/marijana/workspace/Untitled Folder/help.txt"));
		System.out.println("ADRESA");
		System.out.println(getConnectionIpAddress());
		System.out.println("Port");
		System.out.println(getConnectionPort());
		Socket socket = null;
		try {
			socket = new Socket(getConnectionIpAddress(), getConnectionPort());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (fileName.length() != 0) {
			new StartApplicationHandler(fileName, socket).run();
		}
		registryAllTypesOfPackages();
		while (true) {
			try {
				final MessagePackage pack = new PackageReader().read(socket);
				new PackageHandler(pack, socket).run();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
