This version avoids using a PGraphics on the client by wrapping the whole
client program in a class. This also has the benefit of avoiding variable
name conflicts: each player sets the variable names inside his class,
independent of other variable names in other classes.

By using reflection, the system searches for classes that implement "Layer",
which is a minimal interface that just enforces setup() and draw().

The run.sh script builds the source code (one file), compiles it, and launches
it. It's a bash script for Linux systems.

Status: the script succesfully runs the two sample layers found inside
Player1 and Player2. There's no server/client concept so far, and the system
searches for the files in the given folders.

bin/ and *.jar were added to .gitignore to avoid binary blobs inside git.

