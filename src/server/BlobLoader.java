package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import blobs.*;

public class BlobLoader {

	// ArrayList<CJamBlob> blobs = new ArrayList<>();

	public static ArrayList<CJamBlob> createBlobs() {
		String blobPath = new File("").getAbsolutePath() + "\\blobs\\";

		ClassLoader classLoader = BlobLoader.class.getClassLoader();

		ArrayList<CJamBlob> blobs = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(blobPath
					+ "classFiles.txt"));
			String line;
			ArrayList<String> fileNames = new ArrayList<String>();
			while (!((line = br.readLine()).equals(""))) {
				fileNames.add(line);
				Class<CJamBlob> clazz = (Class<CJamBlob>) classLoader
						.loadClass("blobs." + line);
				blobs.add(clazz.newInstance());
			}
		} catch (IOException | ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
		return blobs;
	}

	public static void main(String[] args) {
		System.out.println(BlobLoader.createBlobs().size());
	}
}
