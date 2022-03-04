package Ejercicio2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServidorEj2 {
	private final static int MAX_BYTES = 1400;
	private final static String COD_TEXTO = "UTF-8";
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Ha ocurrido un error, indica el puerto");
			System.exit(1);
		}
		int n_puerto = Integer.parseInt(args[0]);
		String nombre = args[1];
		try {

			DatagramSocket ds_server = new DatagramSocket(n_puerto);
			System.out.printf("El socket de datagramas ha sido creado el para puerto %s.\n", n_puerto);

			System.out.println("Esperando recibir datagramas...");

			byte[] b = new byte[MAX_BYTES];

			DatagramPacket dp_recibido = new DatagramPacket(b, b.length);

			ds_server.receive(dp_recibido);
			String linea = new String(dp_recibido.getData(), 0, dp_recibido.getLength(), COD_TEXTO);
			Pattern patSaludo = Pattern.compile("@hola#(.+)@");
			Matcher m = patSaludo.matcher(linea);
			if (m.find()) {
				nombre = m.group(1);

				InetAddress ia_ip_cliente = dp_recibido.getAddress();
				int npuerto_cliente = dp_recibido.getPort();

				String respuesta = "@hola#" + nombre + "@";
				System.out.println(respuesta);

				byte[] b2 = respuesta.getBytes(COD_TEXTO);

				DatagramPacket dp_enviado = new DatagramPacket(b2, b2.length, ia_ip_cliente, npuerto_cliente);
				ds_server.send(dp_enviado);
				while (true) {

					b = new byte[MAX_BYTES];

					dp_recibido = new DatagramPacket(b, b.length);

					ds_server.receive(dp_recibido);
					linea = new String(dp_recibido.getData(), 0, dp_recibido.getLength(), COD_TEXTO);
					if(linea==".") {
						System.exit(0);
					}
					ia_ip_cliente = dp_recibido.getAddress();
					npuerto_cliente = dp_recibido.getPort();

					respuesta = sc.nextLine();
					
					b = respuesta.getBytes(COD_TEXTO);

					dp_enviado = new DatagramPacket(b2, b2.length, ia_ip_cliente, npuerto_cliente);
					ds_server.send(dp_enviado);
					if(respuesta==".") {
						System.exit(0);;
					}
				}
			} else {
				System.out.println("La línea recibida: "+linea + " es distinta de @hola#(.+)@");
			}

		} catch (SocketException ex) {
			System.out.println("Error de sockets");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("Excepción de E/S");
			ex.printStackTrace();
		}
	}
}
