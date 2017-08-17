package packagereading;

import typesofpackages.CancelPackage;
import typesofpackages.MessagePackage;

/**
 * The Package class is used for managing packages at client side.
 *
 * @author Marijana Tanovic
 */
public class CancelPackageFactory implements PackageFactory {
	/**
	 *
	 * @param header
	 *            Header.
	 * @param body
	 *            Body
	 * @return Message package.
	 */
	@Override
	public MessagePackage readPackage(final byte[] header, final byte[] body) {
		return new CancelPackage(header, body);
	}
}