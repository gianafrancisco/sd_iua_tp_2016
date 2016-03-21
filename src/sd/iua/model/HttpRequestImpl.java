package sd.iua.model;

import java.io.BufferedReader;
import java.util.Properties;

/**
 * Created by francisco on 21/03/2016.
 */
public class HttpRequestImpl implements HttpRequest {
    public HttpRequestImpl(BufferedReader in, Properties prop) {
    }

    @Override
    public String getPlainMessage() {
        return null;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public String getQuery() {
        return null;
    }

    @Override
    public String getVerb() {
        return null;
    }

    @Override
    public boolean isVerbAllowed() {
        return false;
    }

    @Override
    public String getRequestMimeType() {
        return null;
    }
}
