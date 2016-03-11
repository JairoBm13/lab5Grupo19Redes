package Server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ComunicacionUDP extends Thread{
	
	//-------------------------------------------------------
	// Constantes protocolo
	//-------------------------------------------------------
	
	/**
	 * 
	 */
	private final static String C_HOLA = "HOLA";
	private final static String S_INICIO = "INICIO";
	private final static String C_UBICACION = "UBICACION";
	private final static String S_ACK = "OK";
	private final static String C_TERMINAR = "TERMINAR";
	private final static String S_FIN = "FIN";
	private final static String S_ERROR = "ERROR";

	private final static String ARCHIVOS = "docs/udp.csv";
	//-------------------------------------------------------
	// Atributos de trabajo
	//-------------------------------------------------------
	
	private final DatagramPacket sockCliente;
	
	private DatagramSocket conexion;
	
	private int id;
	
	//-------------------------------------------------------
	// Constructor
	//-------------------------------------------------------
	public ComunicacionUDP(DatagramSocket sock, DatagramPacket pack, int nId){
		sockCliente = pack;
		id = nId;
	}
	
	//-------------------------------------------------------
	//
	//-------------------------------------------------------
	public void run(){
		byte[] buffer = sockCliente.getData();
		String mensaje = new String(buffer, 0, buffer.length);
		String[] datos = mensaje.split(":::");
		PrintWriter pW;
		try {
			pW = new PrintWriter(new FileWriter(ARCHIVOS, true));
			pW.println(datos[0]+ ","+datos[1]+","+datos[2]+","+datos[3]);
			pW.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
