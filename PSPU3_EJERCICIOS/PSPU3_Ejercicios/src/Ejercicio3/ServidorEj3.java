package Ejercicio3;

import java.net.*;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServidorEj3 {
	private final static int MAX_BYTES = 1400;
	private final static String COD_TEXTO = "UTF-8";

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("ERROR, indicar: puerto.");
			System.exit(1);
		}
		int n_puerto = Integer.parseInt(args[0]);

		try {

			try (DatagramSocket ds_server = new DatagramSocket(n_puerto)) {
				Random r = new Random();
				NumeroOculto n_oculto = new NumeroOculto(r.nextInt(101));

				while (true) {

					byte[] b_recividos = new byte[MAX_BYTES];
					DatagramPacket dp_recibido = new DatagramPacket(b_recividos, b_recividos.length);

					byte[] b = new byte[MAX_BYTES];
					DatagramPacket dp_enviado = new DatagramPacket(b, b.length);

					ds_server.receive(dp_recibido);

					InetAddress ia_ip_cliente = dp_recibido.getAddress();
					int npuerto_cliente = dp_recibido.getPort();

					String mensaje = new String(dp_recibido.getData(), 0, dp_recibido.getLength(), COD_TEXTO);

					String nombre;
					String n;
					int numero;
					String respuesta;

					Pattern patRespuesta = Pattern.compile("@(.+)#(.*)@");
					Matcher m = patRespuesta.matcher(mensaje);
					if (m.find()) {
						nombre = m.group(1);
						n = m.group(2);
						numero = Integer.parseInt(n);

						int resultado = n_oculto.propuestaNumero(numero);
						if (resultado == -1) {
							respuesta = "@ " + n_oculto.getNumeroOculto() + "#" + numero + "@";
							b = respuesta.getBytes();
							dp_enviado = new DatagramPacket(b, b.length, ia_ip_cliente, npuerto_cliente);
							ds_server.send(dp_enviado);
						} else if (resultado == 1) {
							respuesta = "@Acierto#" + numero + "@";
							n_oculto.setTerminado();
							b = respuesta.getBytes();
							ds_server.setSoTimeout(10000);
							dp_enviado = new DatagramPacket(b, b.length, ia_ip_cliente, npuerto_cliente);
							ds_server.send(dp_enviado);
						} else if (resultado == 0) {
							respuesta = "@Fallo#" + numero + "@";
							b = respuesta.getBytes();
							dp_enviado = new DatagramPacket(b, b.length, ia_ip_cliente, npuerto_cliente);
							ds_server.send(dp_enviado);
						}

					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
class NumeroOculto {
	private int n_oculto;
	private boolean terminado;

	NumeroOculto(int n_oculto) {
		this.n_oculto = n_oculto;
		this.terminado = false;
	}

	synchronized public int getNumeroOculto() {
		return this.n_oculto;
	}

	synchronized public void setTerminado() {
		this.terminado = true;
	}

	synchronized public boolean getTerminado() {
		return this.terminado;
	}

	synchronized int propuestaNumero(int nu) {
		if (this.terminado == true) {
			return -1;
		} else if (nu == this.getNumeroOculto()) {
			return 1;
		} else
			return 0;
	}
}

