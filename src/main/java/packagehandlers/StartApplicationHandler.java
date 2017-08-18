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
     * File where are stored unsent packages..
     */
    private File fileName;

    /**
     * Connection established on socket.
     */
    private Socket socket;

    /**
     * Constructor.
     *
     * @param fileName
     *            File from which packages are loaded.
     * @param socket
     *            Socket to which packages should be sent.
     */
    public StartApplicationHandler(final File fileName, final Socket socket) {
        this.fileName = fileName;
        this.socket = socket;

    }

    /**
     * New thread is run to handle start of application.
     */
    @SuppressWarnings("unchecked")

    public void run() {

        if (fileName.length() == 0) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            final List<DummyPackage> packages = (List<DummyPackage>) ois.readObject();
            packages.forEach(pack -> {
                final MessagePackage p = pack.expired() ? new CancelPackage(pack) : pack;
                System.out.println(p);
                new PackageHandler(p, socket).start();
            });
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
