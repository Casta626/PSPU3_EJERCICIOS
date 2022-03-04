package Ejercicio2;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClienteEj2 {

	private final static int MAX_BYTES = 1400;
	private final static String COD_TEXTO = "UTF-8";
	private final static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		if (args.length < 3) {
			System.err.println("Ha ocurrido un error, indica el puerto");
			System.exit(1);
		}

		String n_host = args[0];

		int n_puerto = Integer.parseInt(args[1]);
		String nombre = args[2];

		try {
			
			DatagramSocket ds_cliente = new DatagramSocket();

			String linea = "@hola#" + nombre + "@";

			InetAddress ia_ip_servidor = InetAddress.getByName(n_host);

			byte[] b = linea.getBytes(COD_TEXTO);

			DatagramPacket dp_enviado = new DatagramPacket(b, b.length, ia_ip_servidor, n_puerto);

			ds_cliente.connect(ia_ip_servidor, n_puerto);
			ds_cliente.send(dp_enviado);

			byte[] recibido = new byte[MAX_BYTES];

			DatagramPacket dp_recibido = new DatagramPacket(recibido, recibido.length);

			ds_cliente.receive(dp_recibido);

			String respuesta = new String(dp_recibido.getData(), 0, dp_recibido.getLength(), COD_TEXTO);
			System.out.printf("El datagrama ha sido recibido de %s:%d: %s\n", dp_recibido.getAddress().getHostAddress(),
					dp_recibido.getPort(), respuesta);
			while (true) {
				ds_cliente.receive(dp_recibido);
				String linea2 = new String(dp_recibido.getData(), 0, dp_recibido.getLength(), COD_TEXTO);
				if(linea2==".") {
					System.exit(0);
				}
				System.out.print("Línea>");
				respuesta = sc.next();
				b = respuesta.getBytes(COD_TEXTO);
				dp_enviado = new DatagramPacket(b, b.length, ia_ip_servidor, n_puerto);

				ds_cliente.connect(ia_ip_servidor, n_puerto);

				ds_cliente.send(dp_enviado);
				if(respuesta==".") {
					System.exit(0);
				}
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

