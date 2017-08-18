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
     * Creates package.
     *
     * @param header
     *            Header of gotten package.
     * @param body
     *            Body of gotten package.
     * @return New message package based on parameters.
     */
    MessagePackage createPackage(byte[] header, byte[] body);
}
