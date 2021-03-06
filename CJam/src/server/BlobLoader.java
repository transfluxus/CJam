package server;

public class BlobLoader {

	public static CJamBlob[] loadBlobs(MainCanvas canvas) {
		@SuppressWarnings("unchecked")
		Class<CJamBlob>[] clazzes = (Class<CJamBlob>[]) MainCanvas.class
				.getDeclaredClasses();
		CJamBlob[] blobs = new CJamBlob[clazzes.length];
		for (int i = 0; i < blobs.length; i++)
			try {
				blobs[i] = clazzes[i].getDeclaredConstructor(MainCanvas.class)
						.newInstance(canvas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return blobs;
	}

}
