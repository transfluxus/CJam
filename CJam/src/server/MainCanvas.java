package server;

import processing.core.PApplet;

public class MainCanvas extends PApplet {

	CJamBlob blobs[];

	@Override
	public void setup() {
		super.setup();
		size(800, 600);
		blobs = BlobLoader.loadBlobs(this);
		for (CJamBlob b : blobs) {
			pushStyle();
			b.setup();
			b.style = PStyleCopy.copyStyle(g.getStyle());
			popStyle();
		}
	}

	@Override
	public void draw() {
		for (CJamBlob b : blobs) {
			pushStyle();
			style(b.style);
			b.draw();
			b.style = PStyleCopy.copyStyle(g.getStyle());
			popStyle();
		}
	}

	public class blob_127_0_0_1 extends CJamBlob {

}
}