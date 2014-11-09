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
public void setup() {  background(0);}public void draw() {  fill(255,100,0);  ellipse(sin(frameCount*0.01f)*width/2, height/2, 30, 30);}public void mousePressed() {}
}public class blob_192_168_2_113 extends CJamBlob {
import client.*;public void setup() {  size(800,600); println(cj.log);background(0);  fill(255);}public void draw() {  ellipse(random(width),height/2+random(height/10),10,10);}
}
}
