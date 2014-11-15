package server;

import processing.core.PApplet;

public class MainCanvasAdd extends MainCanvas {


	@Override
	public void setup() {
		super.setup();
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
