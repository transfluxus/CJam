package test;

import javax.swing.JFrame;

import processing.core.PApplet;

public class TAp extends PApplet {

	private static final long serialVersionUID = 1L;

	int w = 500;
	int h = 500;
	public int b = 200;

	JFrame parent;

	@Override
	public void setup() {
		super.setup();
		size(w, h);
	}

	@Override
	public void draw() {
		background(noise(frameCount / 100f) * 255,
				255 * noise((1 + frameCount) / 100f), b);
	}

	public TAp() {
	}

	public TAp(int w, int h, JFrame frame) {
		super();
		this.w = w;
		this.h = h;
		parent = frame;
	}
}
