In order to run the server perform the next commands:

1. Download the source code.
2. Go to src/ directory (cd src/)
3. Compile the source code with the next command (check that the directory ../out exists):
    /path/to/jdk1.8/bin/javac -d ../out/ sd/iua/ServerHttp.java
4. Go to the parent directory (cd ../)
5. Open the file httpServer.properties and edit the variable web.site.folder with path for "site" directory.
6. Run the server
    /path/to/jdk1.8/bin/java -cp .:out/ sd.iua.ServerHttp

Requirements:

Download jdk1.8 or jdk1.7 from www.oracle.com
