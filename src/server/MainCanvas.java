package server;

import processing.core.PApplet;

public class MainCanvas extends PApplet {

	CJamBlob blobs[];
	public static MainCanvas mc;
	
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
		mc = this;
		CJamServer.MCRunning = true;
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
public void setup() {  background(0);  stroke(255);}public void draw() {  stroke(200,0,200);line(0,height/2,width,random(height));}public void mousePressed() {}
}
}
