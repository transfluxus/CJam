/*
  1. Create a class like:
     class YourName implements Layer { ... }
  2. Add "public void setup()" to your class, but don't call size().
  3. Add "public void draw()" to your class.
  4. Write all your code inside that class.
  5. Have fun!

  Be polite: don't call background() nor
  cover the whole display by other means.
*/

class Antares implements Layer {
  float x;
  public void setup() {
    x = 100;
  }
  public void draw() {
    x = random(width);
    rect(x, height/4, 20, 20);
  }
}
