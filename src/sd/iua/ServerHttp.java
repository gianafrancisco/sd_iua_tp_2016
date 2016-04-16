package sd.iua;

import sd.iua.model.HttpResponseImpl;
import sd.iua.utils.ServerControl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ServerHttp {

	private static Properties prop = new Properties();
	private static List<Socket> clientes;
	private static int maxConn;
	private static ServerSocket server;
	private static ServerSocket serverAdmin;

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
		clientes = ServerControl.getInstance().getSockets();

		Runnable admin = new Runnable() {
			@Override
			public void run() {
				try {
					//serverAdmin = new ServerSocket(puertoAdmin);
					serverAdmin = ServerControl.getInstance().getAdmin(puertoAdmin);
					System.out.printf("'%s' escuchando en: %d%n", prop.getProperty("httpserver.name"), puertoAdmin);
					while (!ServerControl.getInstance().isShutdown()) {
						Socket cliente = serverAdmin.accept();
						System.out.println(cliente.getPort() + " conectado.");
						AdministraServidor administraServidor = new AdministraServidor(cliente, prop, folder);
						administraServidor.start();
					}
				}catch(SocketException e){
					System.out.println("Socket server admin cerrado.");
				}catch (IOException ex){
					throw new RuntimeException(ex);
				}

			}
		};
		/* Creamos el hilo que va administrar el servidor */
		new Thread(admin).start();

		try {
			//server = new ServerSocket(puertoServer);
			server = ServerControl.getInstance().getServer(puertoServer);
			System.out.printf("'%s' escuchando en: %d%n", prop.getProperty("httpserver.name"), puertoServer);
			System.out.printf("Usando '%s' como raíz.%n", folder.getAbsolutePath());

			while (!ServerControl.getInstance().isShutdown()) {
				Socket cliente = server.accept();
				for (Socket t : clientes) {
					if (t != null && t.isClosed()) {
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
		}catch (SocketException ex){
			System.out.println("Socket server cerrado.");
		}catch (IOException ex){
			throw new RuntimeException(ex);
		}
	}
}
