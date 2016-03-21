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

    public HttpRequestImpl(BufferedReader in, Properties prop) {
        this.in = in;
        this.prop = prop;
        sb = new StringBuilder();
        String line;
        try {
            while( (line = in.readLine()) != null){
                sb.append(line+"\r\n");
            }
        } catch (IOException e) {
            sb = null;
        }
        this.line = sb.toString().substring(0,sb.toString().indexOf("\r"));
    }

    @Override
    public String getPlainMessage() {
        return sb.toString();
    }

    @Override
    public String getPath() {
        int begin = ((line.indexOf("//") == -1)?line.indexOf("/"):line.indexOf("/",line.indexOf("//") + 2));
        int end = (line.indexOf("?", begin) == -1)?line.indexOf(" ", begin):line.indexOf("?", begin);
        return line.substring(begin, end);
    }

    @Override
    public String getQuery() {
        int begin = line.indexOf("?");
        if(begin == -1) return "";
        int end = (line.indexOf(" ", begin) == -1)?begin:line.indexOf(" ", begin);
        return line.substring(begin + 1, end);
    }

    @Override
    public String getVerb() {
        String verb = line.substring(0,line.indexOf(" "));
        return verb;
    }

    @Override
    public boolean isVerbAllowed() {

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
