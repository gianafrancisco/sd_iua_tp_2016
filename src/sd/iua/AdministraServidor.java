package sd.iua;

import sd.iua.model.*;
import sd.iua.utils.ServerControl;
import sd.iua.utils.Stats;
import sd.iua.utils.Uptime;

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
		boolean halt = false;
		boolean shutdown = false;
		try {

			if(request.isVerbAllowed()){
				response.addHeader("Server", prop.getProperty("httpserver.name", "HTTP"));
				switch(request.getPath()){
					case "/HALT":
						out.write(response.getResponseHeaderOK().getBytes());
						halt = true;
						break;
					case "/SHUTDOWN":
						out.write(response.getResponseHeaderOK().getBytes());
						shutdown = true;
						break;
					case "/UPTIME":
						out.write(response.getResponseHeaderOK().getBytes());
						out.write(new Uptime().toString().getBytes());
						break;
					case "/REQUESTS":
						out.write(response.getResponseHeaderOK().getBytes());
						out.write(Stats.getInstance().toString().getBytes());
						break;
					default:
						out.write(response.getStatus404().getBytes());
				}
			}else{
				out.write(response.getStatus405().getBytes());
			}

			out.flush();
			out.close();
			in.close();
			socket.close();

			if(shutdown) {
				try {
					ServerControl.getInstance().shutdown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(halt){
				ServerControl.getInstance().halt();
			}


		} catch (IOException e) {
			try {
				out.write(response.getStatus500().getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		System.out.printf("[%d] atendido!", socket.getLocalPort());
	}
}