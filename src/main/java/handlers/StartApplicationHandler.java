package handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.BlockingQueue;

import packages.MessagePackage;

/**
 * The StartApplicationHandler class is used to handle starting application.
 *
 * <p>
 * New start of application requires to check whether some packages from server are received, but not handled in previous
 * application running sessions. If there are, this class read file where those information are stored and handles packages in
 * proper way.
 * </p>
 *
 * @author Marijana Tanovic
 */
public class StartApplicationHandler extends Thread {

    /**
     * File where are stored unsent packages.
     */
    private File fileName;

    /**
     * Queue with packages that are read from server.
     */
    private BlockingQueue<MessagePackage> readQueue;

    /**
     * The StartApplicationHandler constructor.
     *
     * @param fileName
     *            File from which packages are loaded.
     * @param readQueue
     *            Queue with packages read from server.
     */
    public StartApplicationHandler(final File fileName, final BlockingQueue<MessagePackage> readQueue) {
        this.fileName = fileName;
        this.readQueue = readQueue;
    }

    /**
     * Read file where are stored unprocessed packages. Each package is loaded, therefore appropriate type of package is stored
     * in queue.
     */
    @SuppressWarnings("unchecked")
    public void run() {
        if (fileName.length() == 0) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            final BlockingQueue<MessagePackage> queue = (BlockingQueue<MessagePackage>) ois.readObject();
            queue.forEach(pack -> readQueue.add(pack.load()));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
