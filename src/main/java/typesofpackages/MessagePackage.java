package typesofpackages;

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
	 * Serial sta god.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Header in bytes.
	 */
	private byte[] header;
	/**
	 * Body in bytes.
	 */
	private byte[] body;

	/**
	 *
	 * @param header
	 *            Message header.
	 *
	 * @param body
	 *            Message body.
	 */
	MessagePackage(final byte[] header, final byte[] body) {
		this.header = header;
		this.body = body;
	}

	/**
	 *
	 * @return Body of message as array of bytes.
	 */
	public byte[] getBody() {
		return body;
	}

	/**
	 *
	 * @return Header as array of bytes.
	 */
	public byte[] getHeader() {
		return header;
	}

	/**
	 *
	 * @return Package id.
	 */
	public int getPackageId() {
		return ByteUtils.getNthInteger(header, 0);
	}

	/**
	 *
	 * @return Length of package.
	 */
	public int getPackageLength() {
		return ByteUtils.getNthInteger(header, 1);
	}

	/**
	 *
	 * @param socket
	 *            Socket.
	 * @throws IOException
	 *             Exception.
	 */
	public void send(final Socket socket) throws IOException {
		ByteUtils.writeByteArrayToSocket(socket, header);
		ByteUtils.writeByteArrayToSocket(socket, body);
	}

}