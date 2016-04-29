package sd.iua.model;

import sd.iua.model.HttpUtil;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.*;

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
        File f = new File(folder, virtualPath);
        FileInputStream fis = new FileInputStream(f);
        byte bytes[] = new byte[(int)f.length()];
        fis.read(bytes,0,bytes.length);
        fis.close();
        return bytes;
    }

    @Override
    public boolean fileExists(String virtualPath) {
        File f = new File(folder, virtualPath);
        /* Preguntamos si el archivo o directorio existe */
        return f.exists();
    }
}
