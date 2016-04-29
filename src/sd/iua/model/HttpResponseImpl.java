package sd.iua.model;

import sd.iua.utils.Stats;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by francisco on 21/03/2016.
 */
public class HttpResponseImpl implements HttpResponse {

    public static final String HTTP_1_1_404_NOT_FOUND = "HTTP/1.1 404 Not Found\r\n";
    public static final String HTTP_1_1_405_METHOD_NOT_ALLOWED = "HTTP/1.1 405 Method Not Allowed\r\n";
    public static final String HTTP_1_1_500_INTERNAL_SERVER_ERROR = "HTTP/1.1 500 Internal Server Error\r\n";
    public static final String HTTP_1_1_503_SERVICE_UNAVAILABLE = "HTTP/1.1 503 Service Unavailable\r\n";

    private List<Header> header = new ArrayList<>();

    @Override
    public String getStatus404() {
        /* Usamos un sigleton para contabilizar los mensajes de respuesta que el server fue resolviendo */
        Stats.getInstance().status404();
        return HTTP_1_1_404_NOT_FOUND;
    }

    @Override
    public String getStatus405() {
        Stats.getInstance().status405();
        return HTTP_1_1_405_METHOD_NOT_ALLOWED;
    }
    @Override
    public String getStatus500() {
        Stats.getInstance().status500();
        return HTTP_1_1_500_INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getStatus503() {
        Stats.getInstance().status503();
        return HTTP_1_1_503_SERVICE_UNAVAILABLE;
    }

    @Override
    public void addHeader(String name, String value) {
        header.add(new Header(name, value));
    }

    private String headerToString() {
        StringBuilder sb = new StringBuilder();
        for (Header h: header) {
            sb.append(h.toString());
        }
        return sb.toString();
    }

    @Override
    public String getHeaders() {
        return headerToString();
    }

    @Override
    public String getResponseHeaderOK() {
        Stats.getInstance().status200();
        return "HTTP/1.1 200 OK\r\n"+headerToString()+"\r\n";
    }

    private class Header {
        private String name, value;
        public Header(String name, String value){
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return name+": "+value+"\r\n";
        }
    }


}
