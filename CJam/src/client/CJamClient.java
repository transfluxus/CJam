package client;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import processing.core.PApplet;
import processing.net.Client;

public class CJamClient {

	final PApplet ap;
	// for debugging
	public static Client client;
	final static int port = 30303;

	boolean written;
	static boolean autoBg = false;
	static int autoBgColor;

	public static int msgEndMarker = "###ENDOFMSG".hashCode();
	public static String delMarker = "//DEL";

	static String[] deleteLines = { "initCJam", "import", "image", "autoBg",
			"CJam." };

	public static Logger log = Logger.getAnonymousLogger();

	public static CJamClient initCJam(PApplet ap, String serverIp) {
		return new CJamClient(ap, serverIp,port);
	}	
	
	public static CJamClient initCJam(PApplet ap, String serverIp,int port) {
		return new CJamClient(ap, serverIp,port);
	}

	public CJamClient(PApplet ap, String serverIp,int port) {
		this.ap = ap;
		
		log.setLevel(Level.INFO);
		// log.setLevel(Level.WARNING);
		try {
			log.info("connecting to port "+port+"...");
			client = new Client(ap, serverIp, port);
			ap.registerMethod("post", this);
			if (!client.active())
				throw new java.net.ConnectException("nope");
			log.info("connected... getting window size");
			client.write("init");
			client.write(msgEndMarker);

			int timer = 20;
			while(client.available() == 0 && timer > 0) {
				Thread.sleep(100);
				timer--;
			}
			if(client.available()>0) {
				System.out.println();
				String[] size = client.readString().split(" ");
				int szX = Integer.valueOf(size[0]);
				int szY = Integer.valueOf(size[1]);
				ap.size(szX,szY);
				log.info("Received size: "+szX+"x"+szY);
			} else {
				System.out.println("Server didn't send size. gonna use 800x600");
				ap.size(800, 600);
			}
			
		} catch (Exception exc) {
			System.err.println("No Server. You are on your own");
		}
		
	}

	public void post() {
		if (!written && client.active()) {
			log.info("sending");
			String pde = readPDE();
			log.info(pde);
			client.write(pde);
			client.write(msgEndMarker);
			written = true;
			log.info("sent!");
		}
		if (autoBg)
			ap.background(autoBgColor);
		// image(pg, 0, 0);
		// ap.unregisterMethod("post", this);
	}

	public String readPDE() {
		File sketchpath_ = new File(ap.sketchPath);
//		String mainPde = ap.sketchPath.substring(ap.sketchPath
//				.lastIndexOf(System.getProperty("file.separator")) + 1)
//				+ ".pde";
//		log.info("main file: " + mainPde);
		File[] files = sketchpath_.listFiles();
		StringBuilder sb = new StringBuilder();
		for (File f : files) {
			if (f.isFile() 
					&&
					f.getName().endsWith(".pde")) {
					//f.getName().equals(mainPde)) {
				String lines[] = PApplet.loadStrings(f);
				log.info("reading: " + f.getName());
				for (String l : lines) {
					if (l.contains("setup()"))
						l = "public " + l;
					else if (l.contains("draw()"))
						l = "public " + l;
					else if (l.contains("mousePressed()"))
						l = "public " + l;
					else if (l.contains("keyPressed()"))
						l = "public " + l;
					else
						for (String del : deleteLines)
							if (l.contains(del)) {
								l = "//DEL";
								break;
							}
					sb.append(l + System.getProperty("line.separator"));
				}
			}
		}
		return sb.toString();
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
