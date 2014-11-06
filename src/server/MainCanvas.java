package server;


import processing.core.PApplet;

public class MainCanvas extends PApplet {

	CJamBlob blobs[];

	@Override
	public void setup() {
		super.setup();
		size(800, 600);
		//blobs = BlobLoader.createBlobs();
		@SuppressWarnings("unchecked")
		Class<CJamBlob>[] clazzes = (Class<CJamBlob>[]) this.getClass()
				.getDeclaredClasses();
		blobs = new CJamBlob[clazzes.length];
		for (int i = 0; i < blobs.length; i++)
			try {
				blobs[i] = clazzes[i].getDeclaredConstructor(this.getClass())
						.newInstance(this);
				blobs[i].setup();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public void draw() {
		for (CJamBlob b : blobs) {
			b.draw();
			//image(b.getPG(), 0, 0);
		}
	}


public class blob_127_0_0_1 implements CJamBlob {

}
}