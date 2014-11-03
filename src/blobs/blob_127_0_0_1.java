package blobs;

import processing.core.*;
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

import server.*;

public class blob_127_0_0_1 implements CJamBlob {

	PGraphics pg;

	@Override
	public void setup() {

		pg.beginDraw();
		pg.background(0);
		pg.endDraw();
	}

	@Override
	public void draw() {
		// image(pg,0,0);
	}

	@Override
	public PGraphics getPG() {
		return null;
	}
}