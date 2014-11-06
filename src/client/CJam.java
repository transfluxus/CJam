package client;

import java.io.File;

import processing.core.PApplet;
import processing.net.Client;

public class CJam {

	final PApplet ap;
	// for debugging
	public static Client client;
	final static int port = 30303;

	boolean written;
	static boolean autoBg = false;
	static int autoBgColor;

	public static int msgEndMarker = "###ENDOFMSG".hashCode();

	static String[] deleteLines = { "initCJam", "import client.CJam", "image",
			"autoBg" };

	public static CJam initCJam(PApplet ap, String serverIp) {
		return new CJam(ap, serverIp);
	}

	public CJam(PApplet ap, String serverIp) {
		this.ap = ap;
		ap.size(800, 600);
		try {
			client = new Client(ap, serverIp, port);
			ap.registerMethod("post", this);
			if (!client.active())
				throw new java.net.ConnectException("nope");
		} catch (Exception exc) {
			System.err.println("No Server. You are on your own");
		}
	}

	public void post() {
		if (!written && client.active()) {
			ap.println("sending");
			client.write(readPDE());
			client.write(msgEndMarker);
			written = true;
			ap.println("sent!");
		}
		if (autoBg)
			ap.background(autoBgColor);
		// image(pg, 0, 0);
		// ap.unregisterMethod("post", this);
	}

	public String readPDE() {
		File sketchpath_ = new File(ap.sketchPath);
		File[] files = sketchpath_.listFiles();
		StringBuilder sb = new StringBuilder();
		for (File f : files) {
			// the ! CJam thing at the end can go later
			// println(f.getName());
			if (f.isFile() && f.getName().endsWith(".pde")
					&& !(f.getName().equals("CJam.pde"))) {
				String lines[] = ap.loadStrings(f);
				// println("reading: " + f.getName());
				for (String l : lines) {
					// println(l);
					if (l.contains("setup()")) {
						l = "public " + l;
						// println("setup found");
					} else if (l.contains("draw()"))
						l = "public " + l;
					else {
						for (String del : deleteLines)
							if (l.contains(del)) {
								l = "";
								break;
							}
					}
					// else if (l.contains("initCJam("))
					// l = "";
					// else if (l.contains("import client.CJam"))
					// l = "";
					// else if (l.contains("autoBg(")) // should be a static
					// method
					// // of CJam, is then easier
					// // to filter
					// l = "";
					// // l = "pg = parent.createGraphics(800,600);";
					// else if (l.contains("image("))
					// l = "";
					sb.append(l + System.getProperty("line.separator"));
				}
				// since only one file is supported atm return it
				return sb.toString();
			}
		}
		return "";
	}

	public static void autoBg(int c) {
		autoBg = true;
		autoBgColor = c;
	}

	public static void setName(String name) {
		if (client.active()) {
			client.write("name:" + name);
			client.write(msgEndMarker);
		}
	}
}
