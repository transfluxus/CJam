## CJam
====

CJam is a Java/Processing framework for collaborative creative coding. It consists of a server and a number of clients. The clients create processing sketeches and send them to the server. The server runs all the sketches on one big sketch, where every client sketch becomes one layer of the whole




* add the Processing CJam Library (u know it)

* open the example

basically next to the import you just need the line:
CJam.initCJam(this, "127.0.01"); // put the IP of the server
 
* Start the CJamServer from Eclipse
===================================


The Server runs with default settings or can be defined in the "server.properties" file. 
Properties that can be set are:
- port
- deleteOldBlobs
- writeStandAlone
- deleteOldStandalones
- canvasUdpateRate
- updateTimeout

everytime a client starts its sketch it is sent to the server
which restarts after a certain number of submits or a timeout

mainPath: root folder of the project
blobPath: main/blobs
setupFilesPath: main/setupFiles
innerClassPath: main/innerClasses

mainCanvasTxt: setupFilesPath/MainCanvas.txt
mainCanvasAddTxt: setupFilesPath/MainCanvasAdd.txt
mainCanvasJava: main/src/generated/MainCanvas.java
mainCanvasAddJava: main/src/generated/MainCanvasAdd.java



* Enjoy!