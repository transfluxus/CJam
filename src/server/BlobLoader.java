package server;

public class BlobLoader {

	public static CJamBlob[] loadBlobs(MainCanvas canvas) {
		@SuppressWarnings("unchecked")
		Class<CJamBlob>[] clazzes = (Class<CJamBlob>[]) canvas.getClass()
				.getDeclaredClasses();
		CJamBlob[] blobs = new CJamBlob[clazzes.length];
		for (int i = 0; i < blobs.length; i++)
			try {
				blobs[i] = clazzes[i].getDeclaredConstructor(canvas.getClass())
						.newInstance(canvas);
				blobs[i].setup();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return blobs;
	}

}
