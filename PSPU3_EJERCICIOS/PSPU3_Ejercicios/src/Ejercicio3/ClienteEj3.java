package Ejercicio3;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClienteEj3 {
	private final static int MAX_BYTES = 1400;
	private final static String COD_TEXTO = "UTF-8";

	public static void main(String[] args) throws UnknownHostException {
		if (args.length < 3) {
			System.err.println("ERROR, indicar: host_servidor puerto nombre");
			System.exit(1);
		}

		String n_host = args[0];
		int n_puerto = Integer.parseInt(args[1]);
		String nombre = args[2];
		InetAddress ia_ip_servidor = InetAddress.getByName(n_host);

		try {
			DatagramSocket ds_cliente = new DatagramSocket();
			ds_cliente.connect(ia_ip_servidor, n_puerto);
			
			Random r = new Random();
			byte[] b = new byte[MAX_BYTES];
			DatagramPacket dp_enviado;
			boolean fin = false;
			while(!fin) {
				int aleatorio = r.nextInt(101);
				String propuesta = "@" + nombre + "#" + aleatorio + "@";
				b = propuesta.getBytes();
				dp_enviado = new DatagramPacket(b,b.length, ia_ip_servidor, n_puerto);
				
				byte[] b2 = new byte[MAX_BYTES];
				DatagramPacket dp_recibido = new DatagramPacket(b2, b2.length);
				
				ds_cliente.receive(dp_recibido);
				
				String mensaje = new String(dp_recibido.getData(), 0, dp_recibido.getLength(), COD_TEXTO);
				
				String texto;
				String n_string;
				int numero;
				
				Pattern patRespuesta = Pattern.compile("@(.+)#(.*)@");
				Matcher m = patRespuesta.matcher(mensaje);
				
				if(m.find()) {
					texto = m.group(1);
					n_string = m.group(2);
					numero = Integer.parseInt(n_string);
					
					if(texto.equals("Acierto")) {
						System.out.println(numero + " -Acierto.");
					}else if(texto.equals("Fallo")) {
						System.out.println(numero + " -Fallo.");
					}else {
						System.out.println(numero + " -El juego ha terminado.");
					}
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