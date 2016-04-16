package sd.iua.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by francisco on 16/04/16.
 */
public class ServerControl {
    private static ServerControl ourInstance = new ServerControl();
    private boolean halt;
    private List<Socket> sockets;
    private ServerSocket server;
    private ServerSocket admin;


    public static ServerControl getInstance() {
        return ourInstance;
    }

    private boolean shutdown;

    private ServerControl() {
        shutdown = false;
        halt = false;
        sockets = new ArrayList<>();
    }

    public synchronized void shutdown() throws IOException, InterruptedException {
        shutdown = true;
        server.close();
        admin.close();
        for (Socket t : sockets) {
            if ( t!=null && !t.isClosed()) {
                /* Esperamos a que las conexiones en curso hayan terminado */
                while(!t.isClosed()){
                    Thread.sleep(1000);
                }
            };
        }

    }

    public boolean isShutdown(){ return shutdown;}

    public void halt() throws IOException {
        halt = shutdown = true;
        server.close();
        admin.close();
        for (Socket t : sockets) {
            if ( t!=null && !t.isClosed()) {
                t.close();
            };
        }

    }

    public boolean isHalt() {return halt;}

    public List<Socket> getSockets(){return sockets;}

    public ServerSocket getServer(int port) throws IOException {
        server = new ServerSocket(port);
        return server;
    }

    public ServerSocket getAdmin(int port) throws IOException {
        admin = new ServerSocket(port);
        return admin;
    }
}
