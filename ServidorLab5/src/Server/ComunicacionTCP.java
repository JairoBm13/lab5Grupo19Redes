package Server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ComunicacionTCP extends Thread{
	
	// Constantes de protocolo
	private final static String C_HOLA = "HOLA";
	private final static String S_INICIO = "INICIO";
	private final static String C_UBICACION = "UBICACION";
	private final static String S_ACK = "OK";
	private final static String C_TERMINAR = "TERMINAR";
	private final static String S_FIN = "FIN";
	private final static String S_ERROR = "ERROR";
	//------------------------------------------------------------------------------
	
	private final Socket sockCliente;
	
	private InputStream in;
	
	private OutputStream out;
	
	public ComunicacionTCP(Socket cl){
		sockCliente = cl;
	}
	
	/**
	 * Metodo auxiliar para leer mensaje por el socket e imprimir en consola los mensajes de comunicacion
	 */
	public String readBR(BufferedReader br) throws Exception{
		String msj = br.readLine();
		System.out.println("CLI: " + msj);
		return msj;
	}
	
	/**
	 * Metodo auxiliar para enviar mensaje por el socket e imprimir en consola los mensajes de comunicacion
	 */
	public void writePW(PrintWriter pw, String msj) throws Exception{
		pw.println(msj);
		System.out.println("SVR: " + msj);
	}
	
	
	public void run(){
		try{
			in = sockCliente.getInputStream();
			out = sockCliente.getOutputStream();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			PrintWriter pw = new PrintWriter(out, true);
			
			String msjCliente = readBR(br);
			if (msjCliente.equals(C_HOLA)){
				writePW(pw, S_INICIO);
				
				msjCliente = readBR(br);
				while(msjCliente.startsWith(C_UBICACION)){
					
					writePW(pw, S_ACK);
					String [] ubicacion = msjCliente.split(":::");
					
					// TODO Guardar en archivo
					
					msjCliente = readBR(br);
					System.out.println(msjCliente);
				}
				
				if (msjCliente.equals(C_TERMINAR)){
					writePW(pw, S_FIN);
				}
			}
			else{
				writePW(pw, S_ERROR);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			
		}finally{
			try{
				out.close();
				in.close();
				sockCliente.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
	}
}
