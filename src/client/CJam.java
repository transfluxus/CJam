package client;

import processing.core.PApplet;
import processing.net.Client;

public class CJam {

	final PApplet ap;
	final Client client;
	final static int port = 30303;

	public CJam(PApplet ap, String serverIp) {
		this.ap = ap;
		client = new Client(ap, serverIp, port);
		try {
			if (client.active())
				ap.registerMethod("post", this);
			else
				throw new java.net.ConnectException("nope");
		} catch (Exception exc) {
			System.err.println("No Server. You are on your own");
		}
	}

	public void post() {
		System.out.println("sending");
		ap.unregisterMethod("post", this);
	}

}
