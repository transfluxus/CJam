import java.lang.reflect.*;

Layer layer;
void setup() {
  size(800, 600, P3D);

  Class<?>[] a = this.getClass().getDeclaredClasses();
  for (int i=0; i<a.length; i++) {
    if (!a[i].isInterface() && Layer.class.isAssignableFrom(a[i])) {
      try {
        Constructor<?> ctor = a[i].getDeclaredConstructor(this.getClass());
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
