package sd.iua;

import sd.iua.exception.ShutdownServidorException;
import sd.iua.model.*;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class AdministraServidor extends Thread {
	private Socket socket;
	private OutputStream out;
	private BufferedReader in;

	private Properties prop;
	private HttpRequest request;
	private HttpResponse response;
	private HttpUtil util;

	public AdministraServidor(Socket socket, Properties prop, File folder) throws IOException {
		this.prop = prop;
		this.socket = socket;
		out = socket.getOutputStream();
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		request = new HttpRequestImpl(in, prop);
		response = new HttpResponseImpl();
		util = new HttpUtilImpl(folder);
	}

	public void run() {
		System.out.printf("Atendiendo [%d]%n", socket.getLocalPort());
		System.out.println(request.toString());
		try {

			if(request.isVerbAllowed()){
				switch(request.getPath()){
					case "/SHUTDOWN":
						response.addHeader("Server", prop.getProperty("httpserver.name", "HTTP"));
						out.write(response.getResponseHeaderOK().getBytes());
						throw new ShutdownServidorException();
					default:
						out.write(response.getStatus404().getBytes());
				}
			}else{
				out.write(response.getStatus405().getBytes());
			}
		} catch (IOException e) {
			try {
				out.write(response.getStatus500().getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
				in.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		System.out.printf("[%d] atendido!", socket.getLocalPort());
	}
}
