package typesofpackages;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import client.Client;
import utils.ByteUtils;

/**
 * The DummyPackage class is used for managing dummy packages at client side.
 *
 * @author Marijana Tanovic
 */
public class DummyPackage extends MessagePackage {
	/**
	 * Serial sta god.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Time of creation.
	 */
	private Date timeOfCreation;

	/**
	 *
	 * @param header
	 *            Message header.
	 *
	 * @param body
	 *            Message body.
	 */
	public DummyPackage(final byte[] header, final byte[] body) {
		super(header, body);
		this.timeOfCreation = new Date();
	}

	/**
	 *
	 * @return Id.
	 */
	public int getId() {
		return ByteUtils.getNthInteger(getBody(), 0);
	}

	/**
	 *
	 * @return Delay.
	 */
	public int getDelay() {
		final int delay = ByteUtils.getNthInteger(getBody(), 0);
		final int elapsedTime = (int) ((timeOfCreation.getTime() - new Date().getTime()) / (1000));
		return delay - elapsedTime;
	}

	/**
	 *
	 * @return True or false, whether package expired.
	 */
	public boolean expired() {
		return getDelay() <= 0;
	}

	/**
	 * Send package to socket.
	 */
	@Override
	public void send(final Socket socket) throws IOException {
		Client.activePackages.add(this);

		try {
			TimeUnit.SECONDS.sleep(getDelay());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		super.send(socket);

		Client.activePackages.remove(this);
	}

}