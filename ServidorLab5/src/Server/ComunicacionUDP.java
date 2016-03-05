package Server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.Socket;

public class ComunicacionUDP extends Thread{
	
private final DatagramPacket sockCliente;
	
	private InputStream in;
	
	private OutputStream out;
	
	public ComunicacionUDP(DatagramPacket pack){
		sockCliente = pack;
	}
}
