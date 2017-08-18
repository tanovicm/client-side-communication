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
