package sd.iua.utils;

/**
 * Created by francisco on 09/04/16.
 * Sigleton utilizado para contabilizar las respuestas del servidor desde cualquier clase sin necesidad de tener que pasar
 * una instancia como parametro constructor.
 */
public class Stats {
    private static Stats ourInstance = new Stats();

    private int _404 = 0;
    private int _200 = 0;
    private int _405 = 0;
    private int _500 = 0;
    private int _503 = 0;


    public static Stats getInstance() {
        return ourInstance;
    }

    private Stats() {

    }

    public void status404(){
        _404++;
    }
    public void status405(){
        _405++;
    }
    public void status200(){
        _200++;
    }
    public void status500(){
        _500++;
    }
    public void status503(){
        _503++;
    }

    @Override
    public String toString() {
        return "Stats {" +
                " Response 200: " + _200 +
                ", Response 404: " + _404 +
                ", Response 405: " + _405 +
                ", Response 500: " + _500 +
                ", Response 503: " + _503 +
                '}';
    }
}
