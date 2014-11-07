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

	public class local1 extends CJamBlob {
public void setup() {  background(0);}public void draw() {  fill(255,100,0);  ellipse(sin(frameCount*0.01f)*width/2, height/2, 30, 30);}public void mousePressed() {}
}
}
