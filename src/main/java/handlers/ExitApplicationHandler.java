package handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

import packages.MessagePackage;

/**
 * The ExitApplicationHandler class is to handle application termination.
 *
 * <p>
 * When application terminates, this instance is created to handle termination. Handling is performed in a way that all packages
 * which are not handled, therefore are in readQueue, processingQueue, writeQueue, are stored in file.
 * </p>
 *
 * @author Marijana Tanovic
 */

public class ExitApplicationHandler extends Thread {

    /**
     * File where are packages stored.
     */
    private File fileName;

    /**
     * Queue with packages read from server.
     */
    private BlockingQueue<MessagePackage> readQueue;

    /**
     * Queue with packages processed and ready to be sent to server.
     */
    private BlockingQueue<MessagePackage> writeQueue;

    /**
     * Queue with packages currently being processed.
     */
    private BlockingQueue<MessagePackage> processingQueue;

    /**
     * ExitApplicationHandler constructor.
     *
     * @param fileName
     *            File where all unprocessed and unsent packages are saved before termination of application.
     * @param readQueue
     *            Queue with all packages that are read from server.
     * @param writeQueue
     *            Queue with packages that should be sent to server.
     * @param processingQueue
     *            Queue with packages which are currently processed.
     */
    public ExitApplicationHandler(final File fileName, final BlockingQueue<MessagePackage> readQueue,
            final BlockingQueue<MessagePackage> writeQueue, final BlockingQueue<MessagePackage> processingQueue) {
        this.fileName = fileName;
        this.readQueue = readQueue;
        this.writeQueue = writeQueue;
        this.processingQueue = processingQueue;
    }

    /**
     * When application receive signal for termination, this method is run. All packages from read, write and processing queue
     * are stored in file.
     */
    public void run() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            writeQueue.addAll(processingQueue);
            writeQueue.addAll(readQueue);
            oos.writeObject(writeQueue);
            System.err.println("Saved packages!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
