package sd.iua.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by francisco on 21/03/2016.
 */
public class HttpResponseTest {

    private HttpResponse httpResponse;

    @Before
    public void setUp() throws Exception {

        httpResponse = new HttpResponseImpl();


    }

    @Test
    public void response_404() throws Exception {
        Assert.assertThat(httpResponse.getStatus404(), is("HTTP/1.1 404 Not Found\r\n"));
    }

    @Test
    public void response_405() throws Exception {
        Assert.assertThat(httpResponse.getStatus405(), is("HTTP/1.1 405 Method Not Allowed\r\n"));
    }

    @Test
    public void response_500() throws Exception {
        Assert.assertThat(httpResponse.getStatus500(), is("HTTP/1.1 500 Internal Server Error\r\n"));
    }

    @Test
    public void response_503() throws Exception {
        Assert.assertThat(httpResponse.getStatus503(), is("HTTP/1.1 503 Service Unavailable\r\n"));
    }

    @Test
    public void response_200() throws Exception {
        httpResponse.addHeader("attr1", "value 1");
        httpResponse.addHeader("attr2", "value 2");
        Assert.assertThat(httpResponse.getResponseHeaderOK(), is("HTTP/1.1 200 OK\r\nattr1: value 1\r\nattr2: value 2\r\n\r\n"));
    }

    @Test
    public void headers() throws Exception {
        httpResponse.addHeader("attr1", "value 1");
        httpResponse.addHeader("attr2", "value 2");
        Assert.assertThat(httpResponse.getHeaders(), is("attr1: value 1\r\nattr2: value 2\r\n"));
    }
}
