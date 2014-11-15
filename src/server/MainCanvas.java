package server;

import processing.core.PApplet;

public class MainCanvas extends PApplet {

	CJamBlob blobs[];
	
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
blobs[0] = new blob_192_168_2_163();
return blobs;}
public class blob_192_168_2_163 extends CJamBlob {

}
}
