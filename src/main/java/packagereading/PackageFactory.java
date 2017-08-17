package packagereading;

import typesofpackages.MessagePackage;

/**
 * Interface for package factory.
 *
 * @author Marijana Tanovic
 *
 */
public interface PackageFactory {
	/**
	 *
	 * @param header
	 *            Header.
	 * @param body
	 *            Body.
	 * @return Message Package.
	 */
	MessagePackage readPackage(byte[] header, byte[] body);
}
