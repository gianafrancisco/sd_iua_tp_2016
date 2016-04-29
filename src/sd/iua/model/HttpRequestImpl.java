package sd.iua.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by francisco on 21/03/2016.
 */
public class HttpRequestImpl implements HttpRequest {

    private BufferedReader in;
    private StringBuilder sb;
    private Properties prop;
    private String line;
    /* Utilizamos el flag para asegurarnos de que vamos a leer la primera linea cuando alguno de los metodos de invocados */
    private boolean readLine = false;

    public HttpRequestImpl(BufferedReader in, Properties prop) {
        this.in = in;
        this.prop = prop;
        sb = new StringBuilder();
        //this.line = sb.toString().substring(0,sb.toString().indexOf("\r"));
    }

    private void getRequestLine(BufferedReader in) {
        String line;
        try {
            while( (line = in.readLine()) != null){
                if(this.line == null) this.line = line;
                sb.append(line+"\r\n");
                if(line.equals("")) break;
            }
        } catch (IOException e) {
            sb = null;
        }
    }

    @Override
    public String getPlainMessage() {
        /* Verificamos si ya hemos leido la primer linea */
        if(!readLine) {
            getRequestLine(in);
            readLine = true;
        }
        return sb.toString();
    }

    @Override
    public String getPath() {
        if(!readLine) {
            getRequestLine(in);
            readLine = true;
        }
        int begin = ((line.indexOf("//") == -1)?line.indexOf("/"):line.indexOf("/",line.indexOf("//") + 2));
        int end = (line.indexOf("?", begin) == -1)?((line.indexOf(" ", begin) == -1)?line.length():line.indexOf(" ", begin)):line.indexOf("?", begin);
        return line.substring(begin, end);
    }

    @Override
    public String getQuery() {
        if(!readLine) {
            getRequestLine(in);
            readLine = true;
        }
        int begin = line.indexOf("?");
        if(begin == -1) return "";
        begin++;
        int end = (line.indexOf(" ", begin) == -1)?line.length():line.indexOf(" ", begin);
        return line.substring(begin, end);
    }

    @Override
    public String getVerb() {
        if(!readLine) {
            getRequestLine(in);
            readLine = true;
        }
        String verb = line.substring(0,line.indexOf(" "));
        return verb;
    }

    @Override
    public boolean isVerbAllowed() {
        /*
        Comparamos solamente los metodos espcificados en el practico.
         */
        switch (getVerb()){
            case "GET":
                return true;
            case "POST":
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getRequestMimeType() {
        String path = getPath();
        String ext = path.substring(path.lastIndexOf("."), path.length());
        return prop.getProperty(ext);
    }
}
