package sd.iua.model;

import sd.iua.model.HttpUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by francisco on 21/03/2016.
 */
public class HttpUtilImpl implements HttpUtil {

    private File folder;

    public HttpUtilImpl(File folder) {
        this.folder = folder;
    }

    @Override
    public byte[] readFile(String virtualPath) throws IOException {
        return new byte[0];
    }

    @Override
    public boolean fileExists(String virtualPath) {
        //File f = new File(folder.getAbsoluteFile() + File.separator + virtualPath);
        File f = new File(folder, virtualPath);
        return f.exists();
    }
}
