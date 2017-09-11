package client;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import handlers.ExitApplicationHandler;
import handlers.StartApplicationHandler;
import packages.MessagePackage;
import utils.PackageReader;

/**
 * The Client class is used to communicate with server.
 *
 * <p>
 * The client class is create connection with server and listens to incoming data. For each received set of data starts thread
 * and leaves to thread further operations.
 * </p>
 *
 * @author Marijana Tanovic
 */
public final class Client {

    /**
     * Server address.
     */
    private String connectionAddress;

    /**
     * Connection port.
     */
    private int connectionPort;

    /**
     * Filename, file that stores packages which couldn't be sent.
     */
    private File fileName;

    /**
     * Utility classes should not have public constructor.
     *
     * <p>
     * In this constructor, file where packages from previous session is stored.
     * </p>
     */
    private Client() {
        fileName = new File("help.txt");
    }

    /**
     * General settings for application.
     *
     * @param args
     *            Arguments for running application.
     */
    public void parseArgs(final String[] args) {
        if (args.length != 2) {
            System.out.println("Proper Usage is: java program adress port");
            System.exit(0);
        }

        connectionAddress = args[0];
        connectionPort = Integer.parseInt(args[1]);

        System.out.println("ADRESA: " + connectionAddress);
        System.out.println("Port: " + connectionPort);
    }

    /**
     * Function reads packages from socket and puts them in list of read packages.
     *
     * @param socket
     *            Socket from which bytes are read.
     * @param readQueue
     *            Queue where read data is saved.
     */
    private static void readFromSocket(final Socket socket, final BlockingQueue<MessagePackage> readQueue) {
        while (true) {
            try {
                final MessagePackage pack = PackageReader.read(socket);
                System.out.println("Received: " + pack);
                readQueue.add(pack);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Takes package from read queue and processes it.
     *
     * @param readQueue
     *            Queue with packages read from server.
     * @param writeQueue
     *            Queue with packages ready to be sent to server.
     * @param processingQueue
     *            Queue with packages currently being process.
     */
    private static void processData(final BlockingQueue<MessagePackage> readQueue,
            final BlockingQueue<MessagePackage> writeQueue, final BlockingQueue<MessagePackage> processingQueue) {
        final ExecutorService executor = Executors.newFixedThreadPool(10);
        executor.execute(() -> {
            while (true) {
                try {
                    final MessagePackage pack = readQueue.take();
                    processingQueue.add(pack);
                    System.out.println("Processing: " + pack);
                    writeQueue.add(pack.process());
                    processingQueue.remove(pack);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * First package from queue is taken and sent to socket.
     *
     * @param socket
     *            Socket to which data should be written to.
     * @param writeQueue
     *            Queue with packages ready to be sent to server.
     */
    private static void writeToSocket(final Socket socket, final BlockingQueue<MessagePackage> writeQueue) {
        while (true) {
            try {
                final MessagePackage pack = writeQueue.take();
                System.out.println("Sending: " + pack);
                System.out.flush();
                pack.send(socket);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <p>
     * Sets up parameters, prevents losing data and loads data from file, since previous termination. New thread is started for
     * reading data from socket, processing packages on client side and writing data back to socket.
     * </p>
     *
     * @param args
     *            Parameters for application.
     */
    public static void main(final String[] args) {
        final Client client = new Client();
        client.parseArgs(args);

        final BlockingQueue<MessagePackage> readQueue = new LinkedBlockingQueue<MessagePackage>();
        final BlockingQueue<MessagePackage> writeQueue = new LinkedBlockingQueue<MessagePackage>();
        final BlockingQueue<MessagePackage> processingQueue = new LinkedBlockingQueue<MessagePackage>();

        Runtime.getRuntime()
                .addShutdownHook(new ExitApplicationHandler(client.fileName, readQueue, writeQueue, processingQueue));

        try (Socket socket = new Socket(client.connectionAddress, client.connectionPort)) {
            final List<Thread> threads = Arrays.asList(new StartApplicationHandler(client.fileName, readQueue),
                    new Thread(() -> readFromSocket(socket, readQueue)), new Thread(() -> writeToSocket(socket, writeQueue)),
                    new Thread(() -> processData(readQueue, writeQueue, processingQueue)));

            threads.forEach(thread -> thread.start());
            threads.forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
