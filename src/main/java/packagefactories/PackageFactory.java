package packagefactories;

import packages.MessagePackage;

/**
 * Interface for package factory.
 *
 * <p>
 * Interface PackageFactory contains method for creating package based on parameters header and body.
 * </p>
 *
 * @author Marijana Tanovic
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
