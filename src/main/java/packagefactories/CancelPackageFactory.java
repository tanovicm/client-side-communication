package packagefactories;

import packages.CancelPackage;
import packages.MessagePackage;

/**
 * The CancelPackageFactory class is used creating cancel package.
 *
 * @author Marijana Tanovic
 */
public class CancelPackageFactory implements PackageFactory {

    /**
     * Creates cancel package from parameters.
     *
     * @param header
     *            Header of package.
     * @param body
     *            Body of package.
     * @return Cancel message package with passed arguments.
     */
    @Override
    public MessagePackage createPackage(final byte[] header, final byte[] body) {
        return new CancelPackage(header, body);
    }
}
