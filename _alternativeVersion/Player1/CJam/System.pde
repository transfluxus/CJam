import java.lang.reflect.*;

Layer layer;
void setup() {
  size(800, 600, P3D);

  Class<?>[] classes = this.getClass().getDeclaredClasses();
  for (int i=0; i<classes.length; i++) {
    if (!classes[i].isInterface() && Layer.class.isAssignableFrom(classes[i])) {
      try {
        Constructor<?> ctor = classes[i].getDeclaredConstructor(this.getClass());
        layer = (Layer)ctor.newInstance(this);
        layer.setup();
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }
  }
}
void draw() {
  background(0);
  if(layer != null) {
    layer.draw();
  }
}
void keyPressed() {
  if (key == 's') {
    println("Push aBe.pde to server now");
  }
}

interface Layer {
  public void setup();
  public void draw();
}
