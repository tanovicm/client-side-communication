package typesofpackages;

import utils.ByteUtils;

/**
 * The Package class is used for managing packages at client side.
 *
 * @author Marijana Tanovic
 */
public class CancelPackage extends MessagePackage {

    /**
     * Serializable class version number.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
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
     * Constructor.
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

}
