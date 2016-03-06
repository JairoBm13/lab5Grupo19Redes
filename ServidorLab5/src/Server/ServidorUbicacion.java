package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorUbicacion {

	/**
	 * Constante que especifica el tiempo máximo en milisegundos que se esperara 
	 * por la respuesta de un cliente en cada una de las partes de la comunicación
	 */
	private static final int TIME_OUT = 10000;
	
	/**
	 * Constante que especifica el numero de threads que se usan en el pool de conexiones.
	 */
	public static final int N_THREADS = 4;

	/**
	 * Puerto en el cual escucha el servidor.
	 */
	public static final int PUERTO = 8080;

	/**
	 * Metodo main del servidor con seguridad que inicializa un 
	 * pool de threads determinado por la constante nThreads.
	 * @param args Los argumentos del metodo main (vacios para este ejemplo).
	 * @throws IOException Si el socket no pudo ser creado.
	 */
	public static void main(String[] args) throws IOException {
		new ServidorUbicacion().iniciarCom();
	}

	/**
	 * Metodo que atiende a los usuarios.
	 */
	public void iniciarCom() {
		final ExecutorService pool = Executors.newFixedThreadPool(N_THREADS);
		Runnable serverRunTCP = new Runnable(){

			@Override
			public void run() {
				ServerSocket servidorSocket = null;
				try{
					servidorSocket = new ServerSocket(PUERTO);
					System.out.println("Listo para recibir conexiones TCP");
					while(true){
						Socket cliente = servidorSocket.accept();

						cliente.setSoTimeout(TIME_OUT);
					
						pool.execute(new ComunicacionTCP(cliente));
					}
				}catch(Exception e){
					System.err.println("Ocurrio un error");
					e.printStackTrace();
				}finally{
					try{
						servidorSocket.close();
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		};

		Runnable serverRunUDP = new Runnable(){

			@Override
			public void run() {
				DatagramSocket servidorSocket = null;
				try{
					servidorSocket = new DatagramSocket(PUERTO);
					System.out.println("Listo para recibir conexiones TCP");
					while(true){
						byte[] buf = new byte[256];
						DatagramPacket cliente = new DatagramPacket(buf, buf.length);
						servidorSocket.receive(cliente);
						
						pool.execute(new ComunicacionUDP(cliente));
					}
					
				}catch(Exception e){
					System.err.println("Ocurrio un error");
					e.printStackTrace();
				}finally{
					try{
						servidorSocket.close();
					}
					catch(Exception e){
						e.printStackTrace();
					}

				}
			}
		};

		Thread serverTCP = new Thread(serverRunTCP);
		serverTCP.start();

		Thread serverUDP = new Thread(serverRunUDP);
		serverUDP.start();

	}


}
