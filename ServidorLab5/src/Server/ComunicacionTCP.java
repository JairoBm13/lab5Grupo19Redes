package Server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ComunicacionTCP extends Thread{
	
	private final Socket sockCliente;
	
	private InputStream in;
	
	private OutputStream out;
	
	public ComunicacionTCP(Socket cl){
		sockCliente = cl;
	}
	
	public void run(){
		
	}
}
