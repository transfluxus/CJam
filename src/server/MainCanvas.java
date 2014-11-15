package server;

import processing.core.PApplet;

public class MainCanvas extends PApplet {

	CJamBlob blobs[];
	//public static MainCanvas mc;

	
	@Override
	public void setup() {
		super.setup();
		size(800, 600);
		blobs = loadBlobs();
		for (CJamBlob b : blobs) {
			pushStyle();
			b.setup();
			b.style = PStyleCopy.copyStyle(g.getStyle());
			popStyle();
		}
		//mc = this;
		//CJamServer.MCRunning = true;
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

		private CJamBlob[] loadBlobs() { 
System.out.println(getClass().getSuperclass().getName());
int n = getClass().getSuperclass().getDeclaredClasses().length;
CJamBlob[] blobs = new CJamBlob[n];
blobs[0] = new blob_127_0_0_1();
return blobs;}
public class blob_127_0_0_1 extends CJamBlob {
public void setup() {  background(0);  stroke(255);}public void draw() {  stroke(200, random(100),0);  line(0, height/2, width, random(height));}public void mousePressed() {}
}
}
