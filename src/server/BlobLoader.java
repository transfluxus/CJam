package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BlobLoader {

	static String mainPath;
	static String blobPath;

	public static ArrayList<CJamBlob> createBlobs() {
		setupPaths();
		ClassLoader classLoader = BlobLoader.class.getClassLoader();

		ArrayList<CJamBlob> blobs = new ArrayList<>();

		try {
			String[] files = new File(blobPath).list();
			System.out.println(blobPath);
			for (String s : files) {
				Class<CJamBlob> clazz = (Class<CJamBlob>) classLoader
						.loadClass("blobs." + s.substring(0, s.length() - 5));
				blobs.add(clazz.newInstance());
			}
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
		return blobs;
	}

	private static void setupPaths() {
		String p = new File("").getAbsolutePath();
		String mainPath = p.substring(0, p.length() - 3);
		String blobPath = mainPath + "\\src\\blobs\\";

	}

	// public static File[] BlobJavaFiles() {
	// setupPaths();
	//
	// }

	public static void main(String[] args) {
		System.out.println(BlobLoader.createBlobs().size());
	}
}
