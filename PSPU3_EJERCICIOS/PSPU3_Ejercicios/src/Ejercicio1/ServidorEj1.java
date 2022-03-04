package Ejercicio1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServidorEj1 {
	private final static int MAX_BYTES = 1400;
	private final static String COD_TEXTO = "UTF-8";

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Ha ocurrido un error, indica el puerto");
			System.exit(1);
		}
		int numero_puerto = Integer.parseInt(args[0]);
		String nombre = args[1];
			try {
	
				DatagramSocket ds_server = new DatagramSocket(numero_puerto);
				System.out.println("El socket de datagramas ha sido creado para el puerto "+numero_puerto);
				System.out.println("Esperando datagramas.");
				byte[] datosRecibidos = new byte[MAX_BYTES];
				DatagramPacket dp_recibido = new DatagramPacket(datosRecibidos, datosRecibidos.length);
				ds_server.receive(dp_recibido);
				String nombre_cliente = "";
	
				String recibido = new String(dp_recibido.getData(), 0, dp_recibido.getLength(), COD_TEXTO);
				Pattern pattern = Pattern.compile("@hola#(.+)@");
				Matcher matcher = pattern.matcher(recibido);
				if (matcher.find()) {
					nombre_cliente = matcher.group(1);
	
					InetAddress ia_ip_cliente = dp_recibido.getAddress();
					int npuerto_cliente = dp_recibido.getPort();
					System.out.println("Datagrama recibo de "+ia_ip_cliente.getHostAddress()+" "+npuerto_cliente+" "+recibido);
					String respuesta = "@hola#"+nombre+"@";
					byte[] byte_respuesta = respuesta.getBytes(COD_TEXTO);
					DatagramPacket dp_enviado = new DatagramPacket(byte_respuesta, byte_respuesta.length, ia_ip_cliente, npuerto_cliente);
					ds_server.send(dp_enviado);
				} else {
					System.out.println("La cadena recibida: "+recibido + " es diferente de @hola#nombre@");
				}
	
					} catch (SocketException ex) {
						System.out.println("Error de sockets");
						ex.printStackTrace();
					} catch (IOException ex) {
						System.out.println("Error de E/S");
						ex.printStackTrace();
					}
		}
}
