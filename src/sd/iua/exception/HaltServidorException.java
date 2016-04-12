package sd.iua.exception;

/**
 * Created by francisco on 23/03/2016.
 */
public class HaltServidorException extends RuntimeException {
    public HaltServidorException(){
        super("Commando /HALT recibido.");
    }
}
