import processing.net.*;
// needs to go into the CJAM and static
PGraphics initCJam(PApplet ap, String serverIp) {
  new CJam(ap, serverIp);
  return ap.createGraphics(ap.width, ap.height);
}

public class CJam {

  PApplet ap;
  Client client;
  int port = 30303;
  boolean written;

  public CJam(PApplet ap, String serverIp) {
    this.ap = ap;
    ap.size(800, 600);
    //    readPDE();
    try {
      client = new Client(ap, serverIp, port);
      if (client.active())
        ap.registerMethod("post", this);
      else throw new java.net.ConnectException("nope");
    } 
    catch(Exception exc) {
      System.err.println("No Server. You are on your own");
    }
  }


  public void post() {
    if (!written) {
      println("sending"    );
      client.write(readPDE()); 
      written = true;
    }
    //    image(pg, 0, 0);
    //    ap.unregisterMethod("post", this);
  }

  public String readPDE() {
    File sketchpath_ = new File(sketchPath);
    File[] files = sketchpath_.listFiles();
    StringBuilder sb = new StringBuilder();
    for (File f : files) {
      // the ! CJam thing at the end can go later
      println(f.getName());
      if (f.isFile() && f.getName().endsWith(".pde") && !(f.getName().equals("CJam.pde"))) {
        String lines[] = loadStrings(f); 
        println("reading: "+f.getName());
        for (String l : lines) {         
          //         println(l);
          if (l.contains("setup()")) {
            l = "public "+l;
            println("setup found");
          } else if (l.contains("draw()"))
            l = "public "+l; 
          else if (l.contains("initCJam("))
            l = "";
          //            l = "pg = parent.createGraphics(800,600);";
          else if (l.contains("image("))
            l = "";
          sb.append(l+System.getProperty("line.separator"));
        }
        // since only one file is supported atm return it
        return sb.toString();
      }
    }
    return "";
  }
}

