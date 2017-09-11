package packagefactories;

import packages.DummyPackage;
import packages.MessagePackage;

/**
 * The DummyPackageFactory class is used for creating dummy package.
 *
 * @author Marijana Tanovic
 */
public class DummyPackageFactory implements PackageFactory {

    /**
     * Creates the dummy package based on header and body.
     *
     * @param header
     *            Header of package.
     * @param body
     *            Body of package.
     * @return Dummy message package based on arguments.
     */
    @Override
    public MessagePackage createPackage(final byte[] header, final byte[] body) {
        return new DummyPackage(header, body);
    }
}
