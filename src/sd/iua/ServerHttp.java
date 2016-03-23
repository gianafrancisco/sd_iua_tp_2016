package sd.iua;

import sd.iua.exception.ShutdownServidorException;
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
		int puertoAdmin = Integer.parseInt(prop.getProperty("admin.port", "8081"));

		File folder = new File(prop.getProperty("web.site.folder"));
		if (!folder.exists() || !folder.isDirectory()) {
			System.err.printf("ERROR: '%s' no es una ruta correcta.%n", folder.getAbsolutePath());
			System.exit(-1);
		}
		maxConn = Integer.parseInt(prop.getProperty("httpserver.maxconn", "10"));
		clientes = new ArrayList<>();

		Runnable servidor = new Runnable() {
			@Override
			public void run() {
				try {
					@SuppressWarnings("resource")
					ServerSocket server = new ServerSocket(puertoServer);
					System.out.printf("'%s' escuchando en: %d%n", prop.getProperty("httpserver.name"), puertoServer);
					System.out.printf("Usando '%s' como raíz.%n", folder.getAbsolutePath());

					while (true) {
						Socket cliente = server.accept();
						for (Thread t : clientes) {
							if (!t.isAlive()) {
								clientes.remove(t);
								break;
							}
							;
						}
						if (clientes.size() < maxConn) {
							System.out.println(cliente.getPort() + " conectado.");
							AtiendeCliente atiendeCliente = new AtiendeCliente(cliente, prop, folder);
							clientes.add(atiendeCliente);
							atiendeCliente.start();
						} else {
							System.err.println("ERROR: se ha llegado a la capacidad maxima de clientes conectados.");
							cliente.getOutputStream().write(new HttpResponseImpl().getStatus500().getBytes());
							cliente.close();
						}
					}
				}catch (IOException ex){
					throw new RuntimeException(ex);
				}
			}
		};
		new Thread(servidor).start();
		AdministradorHandler ah = new AdministradorHandler();

		ServerSocket server = new ServerSocket(puertoAdmin);
		System.out.printf("'%s' escuchando en: %d%n", prop.getProperty("httpserver.name"), puertoAdmin);
		try {
		while (true) {
			Socket cliente = server.accept();
			System.out.println(cliente.getPort() + " conectado.");
			AdministraServidor administraServidor = new AdministraServidor(cliente, prop, folder);
			administraServidor.setUncaughtExceptionHandler(ah);
			administraServidor.start();
		}
		}catch (IOException ex){
			throw new RuntimeException(ex);
		}

	}

	private static class AdministradorHandler implements Thread.UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			if(e instanceof ShutdownServidorException){
				System.out.println(e);
				System.exit(0);
			}
		}
	}

}
