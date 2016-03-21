package sd.iua;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class ServerHttp {

	private static Properties prop = new Properties();

	public static void main(String[] args) throws IOException {
		prop.load(new FileInputStream(new File(System.getProperty("user.dir"), "httpServer.properties")));
		int puertoServer = Integer.parseInt(prop.getProperty("httpserver.port", "8080"));
		File folder = new File(prop.getProperty("web.site.folder"));
		if (!folder.exists() || !folder.isDirectory()) {
			System.err.printf("ERROR: '%s' no es una ruta correcta.%n", folder.getAbsolutePath());
			System.exit(-1);
		}

		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(puertoServer);
		System.out.printf("'%s' escuchando en: %d%n", prop.getProperty("httpserver.name"), puertoServer);
		System.out.printf("Usando '%s' como raíz.%n", folder.getAbsolutePath());

		while (true) {
			Socket cliente = server.accept();
			System.out.println(cliente.getPort() + " conectado.");
			new AtiendeCliente(cliente, prop, folder).start();
		}

	}

}
