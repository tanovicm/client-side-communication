package typesofpackages;

import utils.ByteUtils;

/**
 * The Package class is used for managing packages at client side.
 *
 * @author Marijana Tanovic
 */
public class CancelPackage extends MessagePackage {
	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param header
	 *            Message header.
	 * @param body
	 *            Message body.
	 */
	public CancelPackage(final byte[] header, final byte[] body) {
		super(header, body);
	}

	/**
	 *
	 * @param expiredPackage
	 *            Expired package.
	 */
	public CancelPackage(final DummyPackage expiredPackage) {
		super(ByteUtils.createByteArray(2, Integer.BYTES), ByteUtils.createByteArray(expiredPackage.getId()));
	}

}