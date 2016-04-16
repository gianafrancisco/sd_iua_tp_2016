package sd.iua;

import sd.iua.model.HttpResponseImpl;
import sd.iua.utils.ServerControl;

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
	private static List<Socket> clientes;
	private static int maxConn;

	public static void main(String[] args) throws IOException {
		prop.load(new FileInputStream(new File(System.getProperty("user.dir"), "httpServer.properties")));
		final int puertoServer = Integer.parseInt(prop.getProperty("httpserver.port", "8080"));
		final int puertoAdmin = Integer.parseInt(prop.getProperty("admin.port", "8081"));

		final File folder = new File(prop.getProperty("web.site.folder"));
		if (!folder.exists() || !folder.isDirectory()) {
			System.err.printf("ERROR: '%s' no es una ruta correcta.%n", folder.getAbsolutePath());
			System.exit(-1);
		}
		maxConn = Integer.parseInt(prop.getProperty("httpserver.maxconn", "10"));
		clientes = new ArrayList<>();

		Runnable admin = new Runnable() {
			@Override
			public void run() {
				try {
					ServerSocket server = new ServerSocket(puertoAdmin);
					System.out.printf("'%s' escuchando en: %d%n", prop.getProperty("httpserver.name"), puertoAdmin);
					while (!ServerControl.getInstance().isShutdown()) {
						Socket cliente = server.accept();
						System.out.println(cliente.getPort() + " conectado.");
						AdministraServidor administraServidor = new AdministraServidor(cliente, prop, folder);
						administraServidor.start();
					}
					server.close();
				}catch (IOException ex){
					throw new RuntimeException(ex);
				}

			}
		};
		/* Creamos el hilo que va administrar el servidor */
		new Thread(admin).start();

		try {
			ServerSocket server = new ServerSocket(puertoServer);
			System.out.printf("'%s' escuchando en: %d%n", prop.getProperty("httpserver.name"), puertoServer);
			System.out.printf("Usando '%s' como raíz.%n", folder.getAbsolutePath());

			while (!ServerControl.getInstance().isShutdown()) {
				Socket cliente = server.accept();
				for (Socket t : clientes) {
					if ( t!= null && t.isClosed()) {
						clientes.remove(t);
						break;
					};
				}
				if (clientes.size() < maxConn) {
					System.out.println(cliente.getPort() + " conectado.");
					AtiendeCliente atiendeCliente = new AtiendeCliente(cliente, prop, folder);
					clientes.add(cliente);
					atiendeCliente.start();
				} else {
					System.err.println("ERROR: se ha llegado a la capacidad maxima de clientes conectados.");
					cliente.getOutputStream().write(new HttpResponseImpl().getStatus500().getBytes());
					cliente.close();
				}
			}
			if(ServerControl.getInstance().isHalt()){
				server.close();
				for (Socket t : clientes) {
					if ( t!=null && !t.isClosed()) {
						t.close();
					};
				}
			}else{
				server.close();
				for (Socket t : clientes) {
					if ( t!=null && !t.isClosed()) {
						try {
							/* Esperamos a que las conexiones en curso hayan terminado */
							while(!t.isClosed()){
								Thread.sleep(1000);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					};
				}
			}
		}catch (IOException ex){
			throw new RuntimeException(ex);
		}
	}
}
