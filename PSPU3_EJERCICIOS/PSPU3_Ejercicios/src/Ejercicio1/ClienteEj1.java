package Ejercicio1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClienteEj1 {

	private final static int MAX_BYTES = 1400;
	private final static String COD_TEXTO = "UTF-8";

	public static void main(String[] args) {
		if (args.length < 3) {
			System.err.println("Ha ocurrido un error, indica el puerto");
			System.exit(1);
		}

		String nombre_host = args[0];

		int numero_puerto = Integer.parseInt(args[1]);
		String nombre = args[2];

		try {		
			DatagramSocket ds_cliente = new DatagramSocket();
			String leer = "@hola#" + nombre + "@";
			InetAddress ia_ipservidor = InetAddress.getByName(nombre_host);
			byte[] b = leer.getBytes(COD_TEXTO);
			DatagramPacket dp_enviado = new DatagramPacket(b, b.length, ia_ipservidor, numero_puerto);
			ds_cliente.connect(ia_ipservidor, numero_puerto);
			ds_cliente.send(dp_enviado);
			byte[] byte_datos_recibidos = new byte[MAX_BYTES];

			DatagramPacket dp_recibido = new DatagramPacket(byte_datos_recibidos, byte_datos_recibidos.length);
			ds_cliente.receive(dp_recibido);
			String respuesta = new String(dp_recibido.getData(), 0, dp_recibido.getLength(), COD_TEXTO);
			
			Pattern patSaludo = Pattern.compile("@hola#(.+)@");
			Matcher m = patSaludo.matcher(respuesta);
			if (m.find()) {
				String nombre_servidor = m.group(1);
				System.out.println("Nombre del servidor:"+nombre_servidor);
			System.out.println("Nombre del servidor:"+dp_recibido.getAddress().getHostName());
			}
		} catch (SocketException ex) {
			System.out.println("Excepción de sockets");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("Excepción de E/S");
			ex.printStackTrace();
		}
	}

}