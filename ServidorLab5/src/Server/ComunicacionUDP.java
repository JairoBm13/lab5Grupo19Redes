package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.xml.ws.handler.MessageContext.Scope;

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
	
	private int id;
	
	private PrintWriter pw;
	
	//-------------------------------------------------------
	// Constructor
	//-------------------------------------------------------
	public ComunicacionUDP(DatagramSocket sock, DatagramPacket pack, int nId, PrintWriter nPw){
		sockCliente = pack;
		id = nId;
		pw = nPw;
	}
	
	//-------------------------------------------------------
	//
	//-------------------------------------------------------
	public void run(){
		byte[] buffer = sockCliente.getData();
		String mensaje = new String(buffer, 0, buffer.length);
		String[] datos = mensaje.split(":::");
		System.out.println("Conexión "+id+" por UDP de "+sockCliente.getAddress().getHostAddress()+" - Longitud:"+datos[0]+ ", Latitud: "+datos[1]+", Velocidad: "+datos[2]+", Altitud: "+datos[3]);	
		pw.println(id+","+sockCliente.getAddress().toString()+","+datos[0]+ ","+datos[1]+","+datos[2]+","+datos[3]);
		pw.close();
	}
}
