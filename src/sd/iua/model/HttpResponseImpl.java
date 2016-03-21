package sd.iua.model;

/**
 * Created by francisco on 21/03/2016.
 */
public class HttpResponseImpl implements HttpResponse {
    @Override
    public String getStatus404() {
        return null;
    }

    @Override
    public String getStatus405() {
        return null;
    }

    @Override
    public String getStatus500() {
        return null;
    }

    @Override
    public String getStatus503() {
        return null;
    }

    @Override
    public void addHeader(String name, String value) {

    }

    @Override
    public String getHeaders() {
        return null;
    }

    @Override
    public String getResponseHeaderOK() {
        return null;
    }
}
