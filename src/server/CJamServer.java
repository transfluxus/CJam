package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import processing.core.PApplet;
import processing.net.Client;
import processing.net.Server;

public class CJamServer extends PApplet {

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

	// default-names are blob_ipAddress(where _ is used instead of .)
	HashMap<String, String> ipToName = new HashMap<>();

	// import lines that go into a clientBlobJava file
	String[] writeInitLine;
	String nl = System.getProperty("line.separator");

	@Override
	public void setup() {
		super.setup();
		server = new Server(this, port);
		setFolders();
		File blobFolder = new File(blobPath);
		if (!blobFolder.exists())
			blobFolder.mkdirs();
		// System.out.println(blobPath);
		writeInitLine = loadStrings(new File(setupFilesPath
				+ "BlobJavaImportLines.txt"));
	}

	public static void setFolders() {
		String p = new File("").getAbsolutePath();
		mainPath = p.substring(0, p.length() - 3);
		blobPath = mainPath + "src\\blobs\\";
		setupFilesPath = mainPath + "setupFiles\\";
		innerClassPath = mainPath + "innerClasses\\";
		mainCanvasTxt = new File(setupFilesPath + "mainCanvas.txt");
		mainCanvasJava = new File(mainPath + "\\src\\server\\MainCanvas.java");
	}

	@Override
	public void draw() {
		client = server.available();
		if (client != null)
			clientProcess(client);
	}

	public void clientProcess(Client client) {
		System.out.println("Client msg: "
				+ java.util.Calendar.getInstance().getTime());
		String ip = client.ip();
		String cs = client.readString();
		// dumpToBlobTxt();
		String[] lines = cs.split("\n");
		// println("received: " + cs);
		// println(lines.length);
		if (cs.startsWith("name:")) {
			ipToName.put(ip, cs.substring(5));
			File oldBlobClass = new File("blob_" + ip.replaceAll("\\.", "_"));
			if (oldBlobClass.exists())
				oldBlobClass.delete();
		} else if (!ipToName.containsKey(ip)) {
			String name = "blob_" + ip.replaceAll("\\.", "_");
			System.out.println(name);
			ipToName.put(ip, name);
		}
		String name = ipToName.get(ip);
		File standaloneBlob = new File(blobPath + name + ".java");
		File innerClassFile = new File(innerClassPath + name + ".txt");
		try {
			BufferedWriter standaloneWriter = new BufferedWriter(
					new FileWriter(standaloneBlob));
			BufferedWriter innerClassWriter = new BufferedWriter(
					new FileWriter(innerClassFile));
			// int lInd = 0;
			// for (; !writeInitLine[lInd].equals("//"); lInd++) {
			// String l = writeInitLine[lInd];
			// bw.write(l + nl);
			// }
			BufferedReader standaloneTemplateReader = new BufferedReader(
					new FileReader(setupFilesPath + "BlobJavaImportLines.txt"));
			String l;
			while ((l = standaloneTemplateReader.readLine()) != null)
				standaloneWriter.write(l + nl);
			innerClassWriter.write("public class " + name
					+ " implements CJamBlob {" + nl);
			standaloneWriter.write("public class " + name
					+ " extends PApplet {" + nl);
			for (String line : lines) {
				innerClassWriter.write(line);
				standaloneWriter.write(line);
			}
			standaloneWriter.write(nl);
			innerClassWriter.write(nl);
			// for (lInd++; lInd < writeInitLine.length; lInd++) {
			// bw.write(writeInitLine[lInd] + nl);
			// }
			innerClassWriter.write("}");
			innerClassWriter.close();
			standaloneWriter.write("}");
			standaloneWriter.close();
			println("Written!");
			server.disconnect(client);
			// compile(cFile);
			updateMainCanvas();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateMainCanvas() {
		// ###blobs
		// String[] canvasLines = loadStrings(mainCanvasTxt);
		// try {
		try {
			FileReader reader = new FileReader(mainCanvasTxt);
			FileWriter fw = new FileWriter(mainCanvasJava);
			int read = 0;
			while ((read = reader.read()) != -1)
				fw.write(read);
			reader.close();
			// bw = new BufferedWriter(fw);
			// for (String line : canvasLines)
			// bw.write(line + nl);
			File[] blobFiles = new File(innerClassPath).listFiles();
			for (File blob : blobFiles) {
				System.out.println(blob);
				reader = new FileReader(blob);
				while ((read = reader.read()) != -1)
					fw.write(read);
				reader.close();
			}
			// bw.write("}" + nl);
			fw.write(nl + "}" + nl);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void compile(File f) {
		String s = null;
		try {
			// run the Unix "ps -ef" command
			// using the Runtime exec method:
			Process p = Runtime.getRuntime().exec(
					"javac -cp " + blobPath + "core.jar;" + blobPath
							+ "CJam.jar" + f.getAbsolutePath());

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			// read the output from the command
			// System.out.println("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			// read any errors from the attempted command
			// System.out
			// .println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}

			// System.exit(0);
			// collectClassFiles();
		} catch (IOException e) {
			System.out.println("exception happened - here's what I know: ");
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
