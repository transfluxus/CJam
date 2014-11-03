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
public class test extends PApplet{

PGraphics pg;

public void setup() {
	size(800,600);
}

public void draw() {
if(pg!=null)
	image(pg,0,0);
}
}