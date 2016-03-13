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
	// Atributos de trabajo
	//-------------------------------------------------------
	
	private final DatagramPacket sockCliente;
	
	private PrintWriter pw;
	
	//-------------------------------------------------------
	// Constructor
	//-------------------------------------------------------
	public ComunicacionUDP(DatagramSocket sock, DatagramPacket pack, int nId, PrintWriter nPw){
		sockCliente = pack;
		pw = nPw;
	}
	
	//-------------------------------------------------------
	//
	//-------------------------------------------------------
	public void run(){
		byte[] buffer = sockCliente.getData();
		String mensaje = new String(buffer, 0, buffer.length);
		String[] datos = mensaje.split(":::");
		long nanosec = System.nanoTime();
		pw.println(datos[5]+","+sockCliente.getAddress().toString()+","+datos[0]+ ","+datos[1]+","+datos[2]+","+datos[3]+","+((nanosec - Long.parseLong(datos[4]))));
		System.out.println("Conexión "+datos[5]+" por UDP de "+sockCliente.getAddress().getHostAddress()+" - Longitud:"+datos[0]+ ", Latitud: "+datos[1]+", Velocidad: "+datos[2]+", Altitud: "+datos[3]);
		pw.close();
	}
}
