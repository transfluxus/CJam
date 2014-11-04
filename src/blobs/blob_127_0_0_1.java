package blobs;

import processing.core.*;
/*
 import processing.data.*; 
 import processing.event.*; 
 import processing.opengl.*; 

 import java.util.HashMap; 
 import java.util.ArrayList; 
 import java.io.File; 
 import java.io.BufferedReader; 
 import java.io.PrintWriter; 
 import java.io.InputStream; 
 import java.io.OutputStream; 
 import java.io.IOException;
 */
import server.*;

public class blob_127_0_0_1 implements CJamBlob {

	PGraphics pg;

	@Override
	public void setup() {
		pg = parent.createGraphics(800, 600);
		pg.beginDraw();
		pg.background(0);
		pg.endDraw();
	}

	@Override
	public void draw() {
		pg.beginDraw();
		pg.background(0);
		pg.ellipse(parent.sin(parent.frameCount * 0.01f) * pg.width / 2,
				pg.height / 2, 30, 30);
		pg.endDraw();

	}

	PApplet parent;

	@Override
	public void setParent(PApplet ap) {
		parent = ap;
	}

	@Override
	public PGraphics getPG() {
		return pg;
	}
}