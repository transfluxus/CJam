package server;

import processing.core.PApplet;
import processing.core.PGraphics;

public interface CJamBlob {

	void setup();

	void draw();

	PGraphics getPG();

	void setParent(PApplet ap);
}
