import client.CJam;

void setup() {
  CJam.initCJam(this, "127.0.01");
  background(0);
}

void draw() {
  fill(255,100,0);
  ellipse(sin(frameCount*0.01f)*width/2, height/2, 30, 30);
}