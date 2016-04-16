package sd.iua.utils;

/**
 * Created by francisco on 16/04/16.
 */
public class ServerControl {
    private static ServerControl ourInstance = new ServerControl();
    private boolean halt;

    public static ServerControl getInstance() {
        return ourInstance;
    }

    private boolean shutdown;

    private ServerControl() {
        shutdown = false;
        halt = false;
    }

    public synchronized void shutdown() {
        shutdown = true;
    }

    public boolean isShutdown(){ return shutdown;}

    public void halt() {
        halt = shutdown = true;
    }

    public boolean isHalt() {return halt;}
}
