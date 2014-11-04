package server;

import java.util.ArrayList;

import processing.core.PApplet;

public class MainCanvas extends PApplet {

	ArrayList<CJamBlob> blobs = new ArrayList<>();

	@Override
	public void setup() {
		super.setup();
		size(800, 600);
		blobs = BlobLoader.createBlobs();
		for (CJamBlob b : blobs) {
			b.setParent(this);
			b.setup();
		}
	}

	@Override
	public void draw() {
		for (CJamBlob b : blobs) {
			b.draw();
			image(b.getPG(), 0, 0);
		}
	}

}
