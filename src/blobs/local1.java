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

public class local1 extends PApplet {
public void setup() {  CJam.setName("local1");  background(0);}public void draw() {  fill(255,100,0);  ellipse(sin(frameCount*0.01f)*width/2, height/2, 30, 30);  }void mousePressed() { CJam.client.write(mouseX); }
}