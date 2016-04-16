package sd.iua.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by francisco on 21/03/2016.
 */
public class HttpRequestTest {

    public static final String REQUEST = "GET /index.html HTTP/1.1\r\n" +
            "Host: localhost:8080\r\n" +
            "Connection: keep-alive\r\n" +
            "Cache-Control: max-age=0\r\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n" +
            "Upgrade-Insecure-Requests: 1\r\n" +
            "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko)\r\n" +
            "Chrome/46.0.2490.80 Safari/537.36\r\n" +
            "Accept-Encoding: gzip, deflate, sdch\r\n" +
            "Accept-Language: es-419,es;q=0.8,en;q=0.6,en-US;q=0.4\r\n" +
            "Cookie: JSESSIONID=4CEA7D677BCAE9B6A0B46C3702C4B013; menustate=SubMenu-Ajax\r\n";
    private HttpRequest httpRequest;
    private Properties p;

    @Before
    public void setUp() throws Exception {

        p = new Properties();
        p.put("attr","value");
        p.put(".html","text/html");
        p.put(".png","image/png");
        StringBuilder sb = new StringBuilder(REQUEST);

        BufferedReader bf = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(sb.toString().getBytes())));

        httpRequest = new HttpRequestImpl(bf, p);

    }

    @Test
    public void plain_message() throws Exception {
        Assert.assertThat(httpRequest.getPlainMessage(), is(REQUEST));
    }

    @Test
    public void path() throws Exception {
        Assert.assertThat(httpRequest.getPath(), is("/index.html"));
    }

    @Test
    public void path_test_2() throws Exception {
        String s = "GET http://localhost:8080/imagenes/fondo.png?w=300&h=200 HTTP/1.1\r\n";
        BufferedReader bf1 = createBufferReader(s);
        httpRequest = new HttpRequestImpl(bf1,p);
        Assert.assertThat(httpRequest.getPath(), is("/imagenes/fondo.png"));
    }


    @Test
    public void query() throws Exception {
        Assert.assertThat(httpRequest.getQuery(), is(""));
    }

    @Test
    public void query_test_2() throws Exception {
        String s = "GET http://localhost:8080/imagenes/fondo.png?w=300&h=200 HTTP/1.1\r\n";
        BufferedReader bf1 = createBufferReader(s);
        httpRequest = new HttpRequestImpl(bf1,p);
        Assert.assertThat(httpRequest.getQuery(), is("w=300&h=200"));
    }

    private BufferedReader createBufferReader(String s) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(s.getBytes())));
        return bf;
    }


    @Test
    public void verb() throws Exception {
        Assert.assertThat(httpRequest.getVerb(), is("GET"));
    }

    @Test
    public void mime_type() throws Exception {
        Assert.assertThat(httpRequest.getRequestMimeType(), is("text/html"));
    }

    @Test
    public void mime_type_test2() throws Exception {
        String s = "PUT http://localhost:8080/imagenes.lindas/fondo.png?w=300&h=200 HTTP/1.1\r\n";
        BufferedReader bf1 = createBufferReader(s);
        httpRequest = new HttpRequestImpl(bf1,p);
        Assert.assertThat(httpRequest.getRequestMimeType(), is("image/png"));
    }

    @Test
    public void is_allowed_true() throws Exception {
        Assert.assertThat(httpRequest.isVerbAllowed(), is(true));
    }

    @Test
    public void is_allowed_false() throws Exception {
        String s = "PUT http://localhost:8080/imagenes/fondo.png?w=300&h=200 HTTP/1.1\r\n";
        BufferedReader bf1 = createBufferReader(s);
        httpRequest = new HttpRequestImpl(bf1,p);
        Assert.assertThat(httpRequest.isVerbAllowed(), is(false));
    }

    @Test
    public void is_allowed_true_post_method() throws Exception {
        String s = "POST http://localhost:8080/imagenes/fondo.png?w=300&h=200 HTTP/1.1\r\n";
        BufferedReader bf1 = createBufferReader(s);
        httpRequest = new HttpRequestImpl(bf1,p);
        Assert.assertThat(httpRequest.isVerbAllowed(), is(true));
    }

    @Test
    public void test_query_without_http_1_1() throws Exception {
        String s = "POST http://localhost:8080/imagenes/fondo.png?w=300&h=200\r\n";
        BufferedReader bf1 = createBufferReader(s);
        httpRequest = new HttpRequestImpl(bf1,p);
        Assert.assertThat(httpRequest.isVerbAllowed(), is(true));
        Assert.assertThat(httpRequest.getVerb(), is("POST"));
        Assert.assertThat(httpRequest.getPath(), is("/imagenes/fondo.png"));
        Assert.assertThat(httpRequest.getQuery(), is("w=300&h=200"));
    }

    @Test
    public void test_path_without_http_1_1() throws Exception {
        String s = "POST http://localhost:8080/imagenes/fondo.png\r\n";
        BufferedReader bf1 = createBufferReader(s);
        httpRequest = new HttpRequestImpl(bf1,p);
        Assert.assertThat(httpRequest.isVerbAllowed(), is(true));
        Assert.assertThat(httpRequest.getVerb(), is("POST"));
        Assert.assertThat(httpRequest.getPath(), is("/imagenes/fondo.png"));
    }


}
