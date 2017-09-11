package packages;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import utils.ByteUtils;

/**
 * The MessagePackage class is used for managing packages at client side.
 *
 * @author Marijana Tanovic
 */
public abstract class MessagePackage implements Serializable {

    /**
     * Serializable class version number.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Header as array of bytes.
     */
    private byte[] header;

    /**
     * Body as array of bytes.
     */
    private byte[] body;

    /**
     * MessagePackage constructor.
     *
     * @param header
     *            Header of received package.
     *
     * @param body
     *            Body of received package.
     */
    MessagePackage(final byte[] header, final byte[] body) {
        this.header = header;
        this.body = body;
    }

    /**
     * Getter for body.
     *
     * @return Body of message as array of bytes.
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Getter for header.
     *
     * @return Header as array of bytes.
     */
    public byte[] getHeader() {
        return header;
    }

    /**
     * Getter for package id.
     *
     * @return Package id.
     */
    public int getPackageId() {
        return ByteUtils.getNthInteger(header, 0);
    }

    /**
     * Getter for length of package.
     *
     * @return Length of package.
     */
    public int getPackageLength() {
        return ByteUtils.getNthInteger(header, 1);
    }

    /**
     * Method to send bytes to socket(server).
     *
     * @param socket
     *            Socket where bytes should be sent.
     * @throws IOException
     *             Function writeByteArrayToSocket can throw exception while writing to socket if one is closed.
     */
    public void send(final Socket socket) throws IOException {
        synchronized (socket) {
            ByteUtils.writeByteArrayToSocket(socket, header);
            ByteUtils.writeByteArrayToSocket(socket, body);
        }
    }

    /**
     * Processes package.
     *
     * @return Processed package.
     */
    public abstract MessagePackage process();

    /**
     * Loads package.
     *
     * @return Loaded package.
     */
    public abstract MessagePackage load();

}
