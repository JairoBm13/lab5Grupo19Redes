package grupo19.clientposicionsockettest;

import android.content.Context;


import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.location.LocationListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class ConexionUDP extends AppCompatActivity implements LocationListener{

    private DatagramSocket socket;
    private final static String SERVIDOR_IP = "192.168.0.13";
    private final static int SERVIDOR_PUERTO = 8080;
    private LocationManager locationMan;

    private double longitud;
    private double latitud;
    private double velocidad;
    private double altitud;

    private String ipServidor;
    private int puertoServidor;

    private static int exitoso = 0;
    private static int nPrueba = 0;

    private boolean detener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_udp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String prov = intent.getStringExtra(MainActivity.PROVEEDOR);
        ipServidor = intent.getStringExtra("IP");
        String puerto = intent.getStringExtra("PORT");
        puertoServidor = Integer.parseInt(puerto);
        detener = false;
        locationMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        int hilos = Integer.parseInt(intent.getStringExtra("HILOS"));
        int ramp = Integer.parseInt(intent.getStringExtra("RAMP"));
        int ciclos = Integer.parseInt(intent.getStringExtra("CICLOS"));

        try {
            if (prov.equals(MainActivity.PROV_GPS)) locationMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            else if (prov.equals(MainActivity.PROV_RED))locationMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        }catch(SecurityException e){
            
        }
        for (int j = 0; j < ciclos;j++) {

            Thread[] threads = new Thread[hilos];
            for (int i = 0; i < hilos; i++) {
                Thread t = new Thread() {

                    @Override
                    public void run() {
                        long nanosecI = 0;
                        long nanosecF = 0;
                        boolean fueExitoso = true;
                        try {
                                nanosecI = System.nanoTime();
                                DatagramSocket udpSocket = new DatagramSocket();
                                String locationData = latitud + ":::" + longitud + ":::" + velocidad + ":::" + altitud;
                                byte[] loccationBytes = locationData.getBytes();
                                InetAddress address = InetAddress.getByName(ipServidor);
                                DatagramPacket packet = new DatagramPacket(loccationBytes, locationData.length(), address, puertoServidor);
                                udpSocket.send(packet);

                                byte[] buf = new byte[256];
                                packet = new DatagramPacket(buf, buf.length);
                                udpSocket.receive(packet);
                                byte[] buffer = packet.getData();
                                String mensaje = new String(buffer, 0, buffer.length);
                                if (mensaje.startsWith("OK")){
                                    nanosecF = System.nanoTime();
                                    exitoso++;
                                }

                                if(fueExitoso)
                                    appendLog("1,"+((nanosecF-nanosecI)/1000000));
                                else
                                    appendLog("0, ");



                        }  catch (Exception e) {
                            nPrueba++;
                            fueExitoso = false;
                        }

                        nPrueba++;
                        Log.e("Damnit","Fuck");
                        if(fueExitoso)
                            appendLog("1,"+((nanosecF-nanosecI)/1000000));
                        else
                            appendLog("0, ");
                    }
                };
                threads[i] = t;
            }

            for (int i = 0; i < hilos; i++) {
                Thread t = threads[i];
                t.start();
                try {
                    Thread.sleep((ramp/hilos) * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateUILocation(){
        TextView txtLongitud = (TextView) findViewById(R.id.txtLongitud);
        txtLongitud.setText(longitud + "");
        TextView txtLatitud = (TextView) findViewById(R.id.txtLatitud);
        txtLatitud.setText(latitud + "");
        TextView txtAltitud = (TextView) findViewById(R.id.txtAltitud);
        txtAltitud.setText(altitud + "");
        TextView txtVelocidad = (TextView) findViewById(R.id.txtVelocidad);
        txtVelocidad.setText(velocidad + "");


    }

    @Override
    public void onLocationChanged(Location location) {
        longitud = location.getLongitude();
        latitud = location.getLatitude();
        velocidad = location.getSpeed();
        altitud = location.getAltitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void detener(View view){
        detener = true;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void appendLog(String text)
    {
        File logFile = new File("sdcard/test/UDP80.csv");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
