package packages;

import java.util.concurrent.TimeUnit;

import utils.ByteUtils;

/**
 * The DummyPackage class is used for managing dummy packages.
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
     * DummyPackage Constructor with additional set of time of creation.
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
     * Dummy package is processed waiting for delay to expire.
     */
    @Override
    public MessagePackage process() {
        try {
            TimeUnit.SECONDS.sleep(getDelay());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Loading dummy package is performed based on expiration of package. If package is expired, from dummy package is created
     * new cancel package and returned, otherwise, same package is returned.
     */
    @Override
    public MessagePackage load() {
        return expired() ? new CancelPackage(this) : this;
    }
}
