package grupo19.clientposicionsockettest;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConexionTCP extends AppCompatActivity implements LocationListener{

    private final static String IP = "192.168.0.13";
    private final static int PUERTO = 8080;

    private final static String C_HOLA = "HOLA";
    private final static String S_INICIO = "INICIO";
    private final static String C_UBICACION = "UBICACION";
    private final static String S_ACK = "OK";
    private final static String C_TERMINAR = "TERMINAR";
    private final static String S_FIN = "FIN";
    private final static String S_ERROR = "ERROR";

    private Socket socket;
    private LocationManager locationMan;

    private PrintWriter out;
    private BufferedReader in;

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
        setContentView(R.layout.activity_conexion_tcp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        detener = false;
        Intent intent = getIntent();
        String prov = intent.getStringExtra(MainActivity.PROVEEDOR);
        ipServidor = intent.getStringExtra("IP");
        String puerto = intent.getStringExtra("PORT");
        puertoServidor = Integer.parseInt(puerto);
        locationMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        int hilos = Integer.parseInt(intent.getStringExtra("HILOS"));
        int ramp = Integer.parseInt(intent.getStringExtra("RAMP"));
        int ciclos = Integer.parseInt(intent.getStringExtra("CICLOS"));

        try {
            if (prov.equals(MainActivity.PROV_GPS))
                locationMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            else if (prov.equals(MainActivity.PROV_RED))
                locationMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        } catch (SecurityException e) {

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
                            socket = new Socket(ipServidor, puertoServidor);
                            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            out = new PrintWriter(socket.getOutputStream(), true);
                            out.println(C_HOLA);
                            String menServ = in.readLine();
                            if (S_INICIO.equals(menServ)) {


                                    out.println(C_UBICACION + ":::" + longitud + ":::" + latitud + ":::" + altitud + ":::" + velocidad);
                                    menServ = in.readLine();
                                    if (menServ.startsWith(S_ACK)){
                                        nanosecF = System.nanoTime();
                                        exitoso++;
                                    }


                            }
                            in.close();
                            out.close();
                            socket.close();
                        } catch (IOException e) {
                            nPrueba++;
                            fueExitoso = false;
                            e.printStackTrace();
                        } catch (Exception e) {
                            nPrueba++;
                            fueExitoso = false;
                            e.printStackTrace();
                        }

                        nPrueba++;
                        Log.e("Damnit", "Fuck");
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

    public void updateLocation(){
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
        out.println(C_TERMINAR);
        detener = true;

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void appendLog(String text)
    {
        File logFile = new File("sdcard/test/TCP80.csv");
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
