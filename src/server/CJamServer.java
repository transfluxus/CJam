package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import processing.net.Client;
import processing.net.Server;

public class CJamServer extends PApplet {

	Server server;
	Client client;

	String mainPath;
	// where the java&class files of the clients go
	String blobPath;
	String setupFilesPath;

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
		String p = new File("").getAbsolutePath();
		mainPath = p.substring(0, p.length() - 3);
		blobPath = mainPath + "blobs\\";
		setupFilesPath = mainPath + "setupFiles\\";
		File blobFolder = new File(blobPath);
		if (!blobFolder.exists())
			blobFolder.mkdirs();
		// System.out.println(blobPath);
		writeInitLine = loadStrings(new File(setupFilesPath
				+ "BlobJavaImportLines.txt"));
	}

	@Override
	public void draw() {
		client = server.available();
		if (client != null)
			clientProcess(client);
	}

	public void clientProcess(Client client) {
		String ip = client.ip();
		String cs = client.readString();
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
		File cFile = new File(blobPath + name + ".java");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(cFile));
			for (String l : writeInitLine)
				bw.write(l + nl);
			bw.write("public class " + name + " implements CJamBlob {" + nl);
			for (String line : lines)
				bw.write(line);
			bw.write("}");
			bw.close();
			println("Written!");
			compile(cFile);
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
			System.out.println("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			// read any errors from the attempted command
			System.out
					.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}

			// System.exit(0);
			collectClassFiles();
		} catch (IOException e) {
			System.out.println("exception happened - here's what I know: ");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void collectClassFiles() {
		String[] blobClassFiles = new File(blobPath).list();
		ArrayList<String> classFileList = new ArrayList<>();
		for (String f : blobClassFiles)
			if (f.endsWith(".class"))
				classFileList.add(f);
		saveStrings(mainPath + "classFiles",
				classFileList.toArray(new String[0]));
	}
}
