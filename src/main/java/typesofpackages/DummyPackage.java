package typesofpackages;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import client.Client;
import utils.ByteUtils;

/**
 * The DummyPackage class is used for managing dummy packages at client side.
 *
 * @author Marijana Tanovic
 */
public class DummyPackage extends MessagePackage {

    /**
     * Serializable class version number.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Time of package creation.
     */
    private long timeOfCreation;

    /**
     * Constructor with additional set of time of creation.
     *
     * @param header
     *            Header used for creating dummy package.
     *
     * @param body
     *            Body used for creating dummy package.
     */
    public DummyPackage(final byte[] header, final byte[] body) {
        super(header, body);
        this.timeOfCreation = System.nanoTime();
    }

    /**
     * Getter for id of package.
     *
     * @return Id of package.
     */
    public int getId() {
        return ByteUtils.getNthInteger(getBody(), 0);
    }

    /**
     * Getter for delay.
     *
     * @return Delay for package in seconds.
     */
    public long getDelay() {
        final int delay = ByteUtils.getNthInteger(getBody(), 1);
        final long elapsedTime = ((System.nanoTime() - timeOfCreation) / (1000000000));
        return delay - elapsedTime;
    }

    /**
     * Method for checking whether package has expired.
     *
     * @return True or false, whether package expired.
     */
    public boolean expired() {
        return getDelay() <= 0;
    }

    @Override
    public final String toString() {
        final int delay = ByteUtils.getNthInteger(getBody(), 1);
        return "DummyPackage (id: " + getId() + ", delay: " + delay + ")";
    }

    /**
     * Sends dummy package to socket.
     *
     * @throws IOException
     *             Sending includes writing to socket which may throw exception.
     */
    @Override
    public void send(final Socket socket) throws IOException {

        Client.activePackages.add(this);

        try {
            TimeUnit.SECONDS.sleep(getDelay());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.send(socket);

        Client.activePackages.remove(this);
    }

}
