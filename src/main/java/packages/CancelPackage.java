package packages;

import utils.ByteUtils;

/**
 * The CancelPackage class is used for managing cancel packages.
 *
 * @author Marijana Tanovic
 */
public class CancelPackage extends MessagePackage {

    /**
     * Serializable class version number.
     */
    private static final long serialVersionUID = 1L;

    /**
     * CancelPackage constructor.
     *
     * @param header
     *            Package header.
     * @param body
     *            Package body.
     */
    public CancelPackage(final byte[] header, final byte[] body) {
        super(header, body);
    }

    /**
     * CancelPackage constructor.
     *
     * @param expiredPackage
     *            Expired dummy package.
     */
    public CancelPackage(final DummyPackage expiredPackage) {
        super(ByteUtils.createByteArray(2, 3 * Integer.BYTES), ByteUtils.createByteArray(expiredPackage.getId()));
    }

    @Override
    public final String toString() {
        return "CancelPackage (id: " + getId() + ")";
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
     * Cancel package doesn't require processing, so the same package is returned.
     */
    @Override
    public MessagePackage process() {
        return this;
    }

    /**
     * Loading of cancel package is performed by returning that same package.
     */
    @Override
    public MessagePackage load() {
        return this;
    }
}
