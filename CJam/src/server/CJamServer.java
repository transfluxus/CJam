package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.*;

import processing.core.PApplet;
import processing.net.Client;
import processing.net.Server;
import client.CJam;

public class CJamServer extends PApplet {
	private static final long serialVersionUID = -4544033159723138250L;

	Server server;
	Client client;

	public static String mainPath;
	// where the java&class files of the clients go
	public static String blobPath;
	/**
	 * here are the blob-codes(format that gets into the MainCanvas) These are
	 * just txt files-since they dont run on their own
	 */
	public static String innerClassPath;
	public static String setupFilesPath;
	public static File mainCanvasTxt;
	public static File mainCanvasJava;

	static final int port = 30303;

	public boolean writeStandAlone = true;
	public boolean deleteOldInnerClasses = true;
	public boolean deleteOldStandalones = true;

	// default-names are blob_ipAddress(where _ is used instead of .)
	HashMap<String, String> ipToName = new HashMap<>();

	private final String nl = System.getProperty("line.separator");

	private Logger log = Logger.getGlobal();

	/**
	 * wait until thi number of clients submitted they sketch until the canvas
	 * is updated (and restarted)
	 */
	private final int canvasUdpateRate = 3;

	ArrayList<String> submits = new ArrayList<String>();

	/**
	 * If clients have submitted their sketch (but rate is not) wait most until
	 * updateTimeout for canvas update
	 */
	private final int updateTimeout = 30000;
	private int updateTimer = 0;

	@Override
	public void setup() {
//		super.setup();
		size(400, 100);
		log.setLevel(Level.INFO);
		log.setUseParentHandlers(false);
		Handler h = new ConsoleHandler();
		h.setFormatter(new SimpleFormatter() {
			@Override
			public String format(LogRecord record) {
				System.out.println("na");
				return record.getMessage() + " @T: " + time(record.getMillis())
						+ "\n";
			}
			private String time(long millisecs) {
				SimpleDateFormat date_format = new SimpleDateFormat("HH:mm");
				Date resultdate = new Date(millisecs);
				return date_format.format(resultdate);
			}
		});
		log.addHandler(h);
		server = new Server(this, port);
		try {
			log.info("CJamServer running at port:" + port + " "
					+ InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		setFolders();
		if (deleteOldInnerClasses)
			deleteFilesIn(innerClassPath);
		if (deleteOldStandalones)
			deleteFilesIn(blobPath);
		// System.out.println(blobPath);
	}

	private void deleteFilesIn(String path) {
		File[] oldFiles = new File(path).listFiles();
		for (File f : oldFiles) {
			log.fine(f.getName() + "delted");
			f.delete();
		}
	}

	public static void setFolders() {
		String p = new File("").getAbsolutePath();
		mainPath = p.substring(0, p.length() - 3);
		log.info("mainpath: " + mainPath);
		blobPath = mainPath + "src\\blobs\\";
		setupFilesPath = mainPath + "setupFiles\\";
		innerClassPath = mainPath + "innerClasses\\";
		mainCanvasTxt = new File(setupFilesPath + "mainCanvas.txt");
		mainCanvasJava = new File(mainPath + "\\src\\server\\MainCanvas.java");
	}

	@Override
	public void draw() {
		client = server.available();
		if (client != null) {
			String s = client.readStringUntil(CJam.msgEndMarker);
			if (s != null) {
				clientProcess(client, s.substring(0, s.length() - 1));
			}
		}
		TimoutUpdate();
	}

	private void TimoutUpdate() {
		if (submits.size() >= 1 && millis() - updateTimer >= updateTimeout) {
			updateMainCanvas();
			submits.clear();
		}
	}

	public void clientProcess(Client client, String msg) {
		String ip = client.ip();
		log.fine("Client msg: " + ip + "\n" + msg);
		System.out.println("Client msg: " + ip + "\n" + msg);
		System.out.println(log.getLevel());
		// println("received: " + cs);
		// println(lines.length);
		if (msg.startsWith("name:")) {
			ipToName.put(ip, msg.substring(5));
			File oldBlobClass = new File("blob_" + ip.replaceAll("\\.", "_"));
			if (oldBlobClass.exists())
				oldBlobClass.delete();
			return;
		} else if (!ipToName.containsKey(ip)) {
			String name = "blob_" + ip.replaceAll("\\.", "_");
			log.fine("new Client: " + name);
			System.out.println("new Client: " + name);
			ipToName.put(ip, name);
		}
		String name = ipToName.get(ip);
		File innerClassFile = new File(innerClassPath + name + ".txt");
		String[] lines = msg.split("\n");
		try {
			BufferedWriter innerClassWriter = new BufferedWriter(
					new FileWriter(innerClassFile));
			BufferedWriter standaloneWriter = null;
			if (writeStandAlone) {
				File standaloneBlob = new File(blobPath + name + ".java");
				BufferedReader standaloneTemplateReader = new BufferedReader(
						new FileReader(setupFilesPath
								+ "BlobJavaImportLines.txt"));
				standaloneWriter = new BufferedWriter(new FileWriter(
						standaloneBlob));
				String l;
				while ((l = standaloneTemplateReader.readLine()) != null)
					standaloneWriter.write(l + nl);
				standaloneTemplateReader.close();
				standaloneWriter.write("public class " + name
						+ " extends PApplet {" + nl);
			}
			innerClassWriter.write("public class " + name
					+ " extends CJamBlob {" + nl);
			for (String line : lines) {
				if (line.contains(CJam.delMarker))
					continue;
				innerClassWriter.write(line);
				if (writeStandAlone)
					standaloneWriter.write(line);
			}
			innerClassWriter.write(nl);
			innerClassWriter.write("}");
			innerClassWriter.close();
			if (writeStandAlone) {
				standaloneWriter.write(nl);
				standaloneWriter.write("}");
				standaloneWriter.close();
			}
			println("Written!");
			server.disconnect(client);
			if (submitRateReached(name))
				updateMainCanvas();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateMainCanvas() {
		try {
			FileReader reader = new FileReader(mainCanvasTxt);
			FileWriter fw = new FileWriter(mainCanvasJava);
			int read = 0;
			while ((read = reader.read()) != -1)
				fw.write(read);
			reader.close();
			File[] blobFiles = new File(innerClassPath).listFiles();
			for (File blob : blobFiles) {
				System.out.println(blob);
				reader = new FileReader(blob);
				while ((read = reader.read()) != -1)
					fw.write(read);
				reader.close();
			}
			fw.write(nl + "}" + nl);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean submitRateReached(String clientName) {
		submits.add(clientName);
		if (submits.size() >= canvasUdpateRate) {
			submits.clear();
			return true;
		} else if (submits.size() == 1) {
			updateTimer = millis();
		}
		return false;
	}

	/*
	 * public void compile(File f) { String s = null; try { // run the Unix
	 * "ps -ef" command // using the Runtime exec method: Process p =
	 * Runtime.getRuntime().exec( "javac -cp " + blobPath + "core.jar;" +
	 * blobPath + "CJam.jar" + f.getAbsolutePath());
	 * 
	 * BufferedReader stdInput = new BufferedReader(new InputStreamReader(
	 * p.getInputStream()));
	 * 
	 * BufferedReader stdError = new BufferedReader(new InputStreamReader(
	 * p.getErrorStream()));
	 * 
	 * // read the output from the command //
	 * System.out.println("Here is the standard output of the command:\n");
	 * while ((s = stdInput.readLine()) != null) { System.out.println(s); }
	 * 
	 * // read any errors from the attempted command // System.out //
	 * .println("Here is the standard error of the command (if any):\n"); while
	 * ((s = stdError.readLine()) != null) { System.out.println(s); } } catch
	 * (IOException e) {
	 * System.out.println("exception happened - here's what I know: ");
	 * e.printStackTrace(); System.exit(-1); } }
	 */
}