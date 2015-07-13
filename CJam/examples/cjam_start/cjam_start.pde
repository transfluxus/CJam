import client.CJamClient;

void setup() {
  CJamClient.initCJam(this, "127.0.0.1",6000);
  background(0);
}

void draw() {
  background(0);
  fill(255, 10, 200);
  translate(sin(frameCount*0.01f)*width/2, height/2);
  rotate(frameCount*0.01f);
  rect(0, 0, 30, 30);
}
