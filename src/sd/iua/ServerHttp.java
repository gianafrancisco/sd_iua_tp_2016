package sd.iua;

import sd.iua.model.HttpResponse;
import sd.iua.model.HttpResponseImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ServerHttp {

	private static Properties prop = new Properties();
	private static List<AtiendeCliente> clientes;
	private static int maxConn;

	public static void main(String[] args) throws IOException {
		prop.load(new FileInputStream(new File(System.getProperty("user.dir"), "httpServer.properties")));
		int puertoServer = Integer.parseInt(prop.getProperty("httpserver.port", "8080"));
		File folder = new File(prop.getProperty("web.site.folder"));
		if (!folder.exists() || !folder.isDirectory()) {
			System.err.printf("ERROR: '%s' no es una ruta correcta.%n", folder.getAbsolutePath());
			System.exit(-1);
		}
		maxConn = Integer.parseInt(prop.getProperty("httpserver.maxconn", "10"));
		clientes = new ArrayList<>();



		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(puertoServer);
		System.out.printf("'%s' escuchando en: %d%n", prop.getProperty("httpserver.name"), puertoServer);
		System.out.printf("Usando '%s' como raíz.%n", folder.getAbsolutePath());

		while (true) {
			Socket cliente = server.accept();
			for(Thread t: clientes){
				if(!t.isAlive()){clientes.remove(t); break;};
			}
			if(clientes.size() < maxConn) {
				System.out.println(cliente.getPort() + " conectado.");
				AtiendeCliente atiendeCliente = new AtiendeCliente(cliente, prop, folder);
				clientes.add(atiendeCliente);
				atiendeCliente.start();
			}else{
				System.err.println("ERROR: se ha llegado a la capacidad maxima de clientes conectados.");
				cliente.getOutputStream().write(new HttpResponseImpl().getStatus500().getBytes());
				cliente.close();
			}
		}

	}

}
