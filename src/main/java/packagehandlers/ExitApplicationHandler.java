package packagehandlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import client.Client;

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
     * New thread for handling exiting from application.
     */
    public void run() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(Client.activePackages);
            System.err.println("Saved packages!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter for name of the file where packages should be stored.
     *
     * @return Filename.
     */
    public File getFileName() {
        return fileName;
    }

    /**
     * Setter for name of the file where packages should be stored.
     *
     * @param fileName
     *            Setter.
     */
    public void setFileName(final File fileName) {
        this.fileName = fileName;
    }

}
