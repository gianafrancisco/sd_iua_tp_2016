package sd.iua.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by francisco on 21/03/2016.
 */
public class HttpUtilTest {

    private HttpUtil httpUtil;

    @Before
    public void setUp() throws Exception {
        httpUtil = new HttpUtilImpl(new File("."));
    }

    @Test
    public void file_not_exists() throws Exception {
        Assert.assertThat(httpUtil.fileExists("test/sd/iua/model/index.htm"), is(false));
    }

    @Test
    public void file_exists() throws Exception {
        Assert.assertThat(httpUtil.fileExists("test/sd/iua/model/index.html"), is(true));
    }

    @Test
    public void read_file_content() throws Exception {
        byte content[] = httpUtil.readFile("test/sd/iua/model/index.html");
        Assert.assertThat(content, is("abcdefg".getBytes()));
    }
}
