package server;

import processing.core.PApplet;

public class MainCanvasAdd extends MainCanvas {

	public int n = 3;

	@Override
	public void setup() {
		super.setup();
		println(n);
		// MORE SETUP?
	}

	@Override
	public void draw() {
		// BEFORE blobs
		// background(0);
		// blobs
		super.draw();
		// AFTER blobs
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "server.MainCanvasAdd" });
	}
}
