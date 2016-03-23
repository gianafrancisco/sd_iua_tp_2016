package sd.iua.exception;

/**
 * Created by francisco on 23/03/2016.
 */
public class ShutdownServidorException extends RuntimeException {
    public ShutdownServidorException(){
        super("Commando /SHUTDOWN recibido.");
    }
}
