package packagehandlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import client.Client;
import typesofpackages.MessagePackage;

/**
 * The Package class is used for managing packages at client side.
 *
 * @author Marijana Tanovic
 */

public class ExitApplicationHandler extends Thread {

	/**
	 * Number indicating type of package.
	 */
	private File fileName;

	/**
	 * @param fileName
	 *            Name of the file where packages should be listed.
	 */
	public ExitApplicationHandler(final File fileName) {
		this.setFileName(fileName);
	}

	/**
	 * What to do.
	 */
	public void run() {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		for (MessagePackage pack : Client.activePackages) {
			try {

				fout = new FileOutputStream(fileName);
				oos = new ObjectOutputStream(fout);
				oos.writeObject(pack);

				System.out.println("Done");

			} catch (IOException ex) {

				ex.printStackTrace();

			} finally {

				if (fout != null) {
					try {
						fout.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (oos != null) {
					try {
						oos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

		}
		System.err.println("Caoooo!");
	}

	/**
	 *
	 * @return Filename.
	 */
	public File getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            Setter.
	 */
	public void setFileName(final File fileName) {
		this.fileName = fileName;
	}

}
