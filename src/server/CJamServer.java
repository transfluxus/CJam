package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import processing.core.PApplet;
import processing.net.Client;
import processing.net.Server;
import client.CJamClient;

public class CJamServer extends PApplet {
	private static final long serialVersionUID = -4544033159723138250L;

	// net Server
	private Server server;
	public final static String serverPropertiesFile = "server.properties";

	// path of the Project
	protected static String mainPath;
	// where the java&class files of the clients go
	private static String blobPath;
	/**
	 * here are the blob-codes(format that gets into the MainCanvas) These are
	 * just txt files-since they dont run on their own
	 */
	private static String innerClassPath;
	private static String setupFilesPath;
	private static String generatedJavaFilesPath;
	private static File mainCanvasTxt;
	private static File mainCanvasAddTxt;
	private static File mainCanvasJava;
	private static File mainCanvasAddJava;

	private static int port = 30303;

	private boolean writeStandAlone = true;
	private boolean deleteOldInnerClasses = true;
	private boolean deleteOldStandalones = true;

	// default-names are blob_ipAddress(where _ is used instead of .)
	HashMap<String, String> ipToName = new HashMap<>();

	private final String nl = System.getProperty("line.separator");

	private static Logger log = Logger.getGlobal();

	/**
	 * wait until this number of clients submitted they sketch until the canvas
	 * is updated (and restarted)
	 */
	private int canvasUdpateRate = 1;

	ArrayList<String> submits = new ArrayList<String>();

	/**
	 * If clients have submitted their sketch (but rate is not) wait most until
	 * updateTimeout for canvas update
	 */
	private int updateTimeout = 30000;
	private int updateTimer = 0;

	/**
	 * set when the MainCanvas is running and needs to be destroyed
	 */
	private static boolean MCRunning = false;

	/**
	 * this is the process running the MainCanvasAdd
	 */
	private Process process;

	@Override
	public void setup() {
		super.setup();
		size(1, 1);
		setupLogger();
		setFolders();
		setSettings();
		log.info("**********************************************************");
		server = new Server(this, port);
		try {
			log.info("CJamServer running at port:" + port + " "
					+ InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if (deleteOldInnerClasses)
			deleteFilesIn(innerClassPath);
		if (deleteOldStandalones)
			deleteFilesIn(blobPath);
	}

	private void setupLogger() {
		log.setLevel(Level.ALL);
		log.setFilter(null);
		log.setUseParentHandlers(false);
		ConsoleHandler h = new ConsoleHandler();
		log.addHandler(h);
		h.setFormatter(new SimpleFormatter() {
			@Override
			public String format(LogRecord record) {
				return record.getMessage() //+ " @T: " + time(record.getMillis())
						+ nl;
			}
		});
	}
	
	private void setSettings() {
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(new File(mainPath + File.separator
					+ serverPropertiesFile)));

			Optional<String> port = getOptionalProperty(properties, "port");
			if (port.isPresent()) {
				CJamServer.port = Integer.valueOf(port.get());
				log.info("port from properties-file: " + CJamServer.port);
			}
			// TODO: get this shorter! loop gracefully
			// TODO: CLEARIFY CONFUSING NAMING
			Optional<String> deleteOldBlobs = getOptionalProperty(properties,
					"deleteOldBlobs");
			if (deleteOldBlobs.isPresent()) {
				this.deleteOldInnerClasses = Boolean.valueOf(deleteOldBlobs
						.get());
			}

			Optional<String> deleteOldStandalones = getOptionalProperty(
					properties, "deleteOldStandalones");
			if (deleteOldStandalones.isPresent()) {
				this.deleteOldStandalones = Boolean
						.valueOf(deleteOldStandalones.get());
				log.info("deleteOldStandalones from properties-file: "
						+ this.deleteOldStandalones);
			}

			Optional<String> canvasUdpateRate = getOptionalProperty(properties,
					"canvasUdpateRate");
			if (canvasUdpateRate.isPresent())
				this.canvasUdpateRate = Integer.valueOf(canvasUdpateRate.get());

			Optional<String> updateTimeout = getOptionalProperty(properties,
					"updateTimeout");
			if (updateTimeout.isPresent())
				this.updateTimeout = Integer.valueOf(updateTimeout.get());

		} catch (FileNotFoundException e) {
			System.err
					.println("Properties file not found. staying with default");
		} catch (IOException e) {
			e.printStackTrace();
			e.printStackTrace();
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			System.err.println(nfe.getStackTrace() + " NumberFormatException: "
					+ nl + "Staying with the good old values when compiled");
		}
	}

	private Optional<String> getOptionalProperty(Properties properties,
			String propName) {
		return Optional.ofNullable(properties.getProperty(propName));
	}

	private void deleteFilesIn(String path) {
		File[] oldFiles = new File(path).listFiles();
		for (File f : oldFiles) {
			log.fine(f.getName() + "delted");
			f.delete();
		}
	}

	public static void setFolders() {
		String separator = File.separator;
		
		// mainPath
		mainPath = new File("").getAbsolutePath(); 
		if(mainPath.endsWith("bin"))
			mainPath = mainPath.substring(0,mainPath.length()-3);
		log.info("mainpath: " + mainPath);
		
		blobPath = mainPath + separator + "blobs" + separator;
		createPathIfNotThere(blobPath);
		
		setupFilesPath = mainPath + separator + "setupFiles" + separator;
		createPathIfNotThere(setupFilesPath);

		innerClassPath = mainPath + separator + "innerClasses" + separator;
		createPathIfNotThere(innerClassPath);
		
		// Templates for generated files
		mainCanvasTxt = new File(setupFilesPath + "MainCanvas.txt");
		mainCanvasAddTxt = new File(setupFilesPath + "MainCanvasAdd.txt");
		
		// Generated files
		generatedJavaFilesPath= mainPath + separator + "src" + separator
				+ "server"+separator+"generated" + separator; 
		createPathIfNotThere(generatedJavaFilesPath);
		mainCanvasJava = new File(generatedJavaFilesPath + "MainCanvas.java");
		mainCanvasAddJava = new File(generatedJavaFilesPath + "MainCanvasAdd.java");
	}

	private static void createPathIfNotThere(String pathName) {
		File path = new File(pathName);
		if (!path.exists())
				path.mkdirs();
	}

	@Override
	public void draw() {
		Client client = server.available();
		if (client != null) {
			String s = client.readStringUntil(CJamClient.msgEndMarker);
			if (s != null) {
				clientProcess(client, s.substring(0, s.length() - 1));
			}
		}
		TimoutUpdate();
	}

	/**
	 * checks if the timerupdate is expired to restart the canvas
	 */
	private void TimoutUpdate() {
		if (submits.size() >= 1 && (millis() - updateTimer) >= updateTimeout) {
			updateMainCanvas();
			submits.clear();
		}
	}

	public void clientProcess(Client client, String msg) {
		String ip = client.ip();
		log.info("Client msg: " + ip + "\n" + msg);
		// println("received: " + cs);
		// println(lines.length);
		// Client sends name
		if (msg.startsWith("name:")) {
			ipToName.put(ip, msg.substring(5));
			File oldBlobClass = new File("blob_" + ip.replaceAll("\\.", "_"));
			if (oldBlobClass.exists())
				oldBlobClass.delete();
			return;
		} else if (!ipToName.containsKey(ip)) {
			String name = "blob_" + ip.replaceAll("\\.", "_");
			log.info("new Client: " + name);
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
				if (line.contains(CJamClient.delMarker))
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
			log.info(name + " txtfile written!");
			server.disconnect(client);
			if (submitRateReached(name))
				updateMainCanvas();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates the MainCanvas.java out txt
	 */
	private void updateMainCanvas() {
		try {
			FileReader reader = new FileReader(mainCanvasTxt);
			FileWriter fw = new FileWriter(mainCanvasJava);
			// write from the mainCanvas txt file (template into the java file)
			int read = 0;
			while ((read = reader.read()) != -1)
				fw.write(read);
			reader.close();

			FileReader readerMainCanvasAdd = new FileReader(mainCanvasAddTxt);
			FileWriter writerMainCanvasAdd = new FileWriter(mainCanvasAddJava);
			int read2 = 0;
			while ((read2 = readerMainCanvasAdd.read()) != -1) {
				writerMainCanvasAdd.write(read2);
			}
			readerMainCanvasAdd.close();
			writerMainCanvasAdd.close();

			// some additional line to load the inner classes (no reflection)
			File[] blobFiles = new File(innerClassPath).listFiles();
			fw.write("	private CJamBlob[] loadBlobs() { " + nl
					+ "System.out.println(getClass().getName());" + nl
					+ " Class<?> canvasClazz = getClass();"+ nl
					+ "if(!canvasClazz.getSimpleName().equals(\"MainCanvas\"))"+ nl
					+	"canvasClazz = getClass().getSuperclass();"+ nl
					+ "if(!canvasClazz.getSimpleName().equals(\"MainCanvas\")) {"+ nl
					+"	System.out.println(\"Didn't get MainCanvas class. Work on the setupFiles/MainCanvas.txt template\");"+ nl
					+"	System.exit(1);"+ nl
					+"}"+ nl
					+"int n = canvasClazz.getDeclaredClasses().length;"+nl
					+ "blobs = new CJamBlob[n];"+nl);
			int i = 0;
			for (File blob : blobFiles) {
				String blobName = blob.getName();
				blobName = blobName.substring(0, blobName.length() - 4);
				fw.write("blobs[" + (i++) + "] = new " + blobName + "();" + nl);
			}
			fw.write("return blobs;}" + nl);
			// add the blobs as inner classes
			for (File blob : blobFiles) {
				log.info("adding: " + blob);
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
		startMC();
	}

	private void startMC() {
		System.out.println("compiling");
		ArrayList<File> files = new ArrayList<File>();

		System.out.println(mainCanvasJava);
		System.out.println(mainCanvasAddJava);

		// System.exit( 1 );

		files.add(mainCanvasJava);
		files.add(mainCanvasAddJava);
		boolean success = new Compiler().compile(files);
		System.out.println("compilation: " + success);
		if (!success) {
			System.err.println("Compilation failed. Actual Canvas remains");
			return;
		}
		if (MCRunning)
			process.destroy();

		String p = "java -cp " + mainPath + "bin;" + mainPath
				+ "bin" + File.separator + "core.jar;. "
				+ "server.generated.MainCanvasAdd";
		System.out.println(p);
		try {
			process = Runtime.getRuntime().exec(p);
			MCRunning = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * checks if the number of submits reached canvasUdpateRate. Multiple
	 * submits by the same client will be registered and does not increate the
	 * number of submits
	 *
	 * @param clientName
	 * @return true, if rate is reached
	 */
	private boolean submitRateReached(String clientName) {
		String timeS = ((updateTimeout - (millis() - updateTimer)) / 1000)
				+ "s";
		if (submits.contains(clientName)) {
			log.info(clientName + " updated. Timer expires in " + timeS);
			return false;
		}
		submits.add(clientName);
		if (submits.size() >= canvasUdpateRate) {
			submits.clear();
			return true;
		} else if (submits.size() == 1) {
			updateTimer = millis();
			log.info("1/" + canvasUdpateRate + " starting updateTimer: "
					+ (updateTimeout / 1000) + "s");
		} else {
			log.info(submits.size() + "/" + canvasUdpateRate
					+ " timer expires in: " + timeS);
		}
		return false;
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "server.CJamServer" });
	}
}
