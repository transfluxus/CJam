# Copy the content of base to CJam.java 
# (except the last line, which is a "}")
head -n -1 src/CJam.base > src/CJam.java

# In future versions, the code would be received by the server.
# Here we just scan a folder looking for the user files.
for file in ../*
do
    if [ "$file" != "../Server" ]; 
    then
        # Add the user files to CJam.java 
        cat "$file/CJam/CJam.pde" >> src/CJam.java
    fi
done

# Add the missing "}"
echo "}" >> src/CJam.java

# Compile the program
javac -d bin -sourcepath src -cp libs/core.jar src/CJam.java 

# Run the program
java -cp bin:libs/core.jar:libs/jogl-all.jar:libs/gluegen-rt.jar:libs/jogl-all-natives-linux-i586.jar:libs/gluegen-rt-natives-linux-i586.jar CJam

