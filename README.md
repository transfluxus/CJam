## CJam
====

CJam is a Java/Processing framework for collaborative creative coding. It consists of a server and a number of clients. The clients create processing sketeches and send them to the server. The server runs all the sketches on one big sketch, where every client sketch becomes one layer of the whole

---
* add the Processing CJam Library (u know it)

* open the example
basically next to the import you just need the line:
CJam.initCJam(this, "127.0.01"); // put the IP of the server
 
* Start the CJamServer from Eclipse

everytime a client starts its sketch it is sent to the server
which restarts after a certain number of submits or a timeout

Check the properties for settings.

* Enjoy!