package server;

import processing.core.PApplet;
import processing.core.PGraphics;

public interface CJamBlob {

	static PApplet parent = null;

	void setup();

	void draw();

	PGraphics getPG();
}
