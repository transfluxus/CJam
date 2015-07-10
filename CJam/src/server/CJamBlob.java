package server;

import processing.core.PStyle;

public abstract class CJamBlob {

	abstract public void setup();

	abstract public void draw();

	public PStyle style;

	public void setStyle(PStyle style) {
		this.style = style;
	}

}
