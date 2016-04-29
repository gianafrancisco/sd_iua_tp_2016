package sd.iua.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * Created by francisco on 09/04/16.
 * Utilitario para calcular el uptime del proceso.
 */
public class Uptime {
    private long uptime;

    public Uptime(){
        RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
        uptime = rt.getUptime();
    }

    @Override
    public String toString() {
        return "Uptime {" +
                " uptime = " + uptime +" milliseconds"+
                '}';
    }
}
