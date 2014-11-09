package server;

import processing.core.PFont;
import processing.core.PStyle;

public class PStyleCopy {

	public static PStyle copyStyle(PStyle s) {
		PStyle ns = new PStyle();
		ns.imageMode = s.imageMode;
		ns.rectMode = s.rectMode;
		ns.ellipseMode = s.ellipseMode;
		ns.shapeMode = s.shapeMode;

		ns.blendMode = s.blendMode;

		ns.colorMode = s.colorMode;
		ns.colorModeX = s.colorModeX;
		ns.colorModeY = s.colorModeY;
		ns.colorModeZ = s.colorModeZ;
		ns.colorModeA = s.colorModeA;

		ns.tint = s.tint;
		ns.tintColor = s.tintColor;
		ns.fill = s.fill;
		ns.fillColor = s.fillColor;
		ns.stroke = s.stroke;
		ns.strokeColor = s.strokeColor;
		ns.strokeWeight = s.strokeWeight;
		ns.strokeCap = s.strokeCap;
		ns.strokeJoin = s.strokeJoin;

		// TODO these fellas are inconsistent, and may need to go elsewhere
		ns.ambientR = s.ambientR;
		ns.ambientG = s.ambientG;
		ns.ambientB = s.ambientB;
		ns.specularR = s.specularR;
		ns.specularG = s.specularG;
		ns.specularB = s.specularB;
		ns.emissiveR = s.emissiveR;
		ns.emissiveG = s.emissiveG;
		ns.emissiveB = s.emissiveB;
		ns.shininess = s.shininess;

		ns.textFont = s.textFont;
		ns.textAlign = s.textAlign;
		ns.textAlignY = s.textAlignY;
		ns.textMode = s.textMode;
		ns.textSize = s.textSize;
		ns.textLeading = s.textLeading;

		return ns;
	}
}
