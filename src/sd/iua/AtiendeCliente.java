package sd.iua;

import sd.iua.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Properties;

public class AtiendeCliente extends Thread {
	private Socket socket;
	private OutputStream out;
	private BufferedReader in;

	private Properties prop;
	private HttpRequest request;
	private HttpResponse response;
	private HttpUtil util;

	public AtiendeCliente(Socket socket, Properties prop, File folder) throws IOException {
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
			if (!util.fileExists(request.getPath())) {
				out.write(response.getStatus404().getBytes());
			} else if (!request.isVerbAllowed()) {
				out.write(response.getStatus405().getBytes());
			} else {
				byte[] body = util.readFile(request.getPath());

				response.addHeader("Server", prop.getProperty("httpserver.name", "HTTP"));
				response.addHeader("Content-Type", request.getRequestMimeType());
				response.addHeader("Content-Length:", body.length + "");

				out.write(response.getResponseHeaderOK().getBytes());
				for (byte b : body)
					out.write(b);
			}
		} catch (Exception e) {
			try {
				out.write(response.getStatus500().getBytes());
			}catch (SocketException e1){
				System.out.println("Socker cerrado.");
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
