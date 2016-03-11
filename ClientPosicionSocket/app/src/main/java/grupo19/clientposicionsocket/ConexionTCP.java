package grupo19.clientposicionsocket;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_tcp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String prov = intent.getStringExtra(MainActivity.PROVEEDOR);

        locationMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        try {
            if (prov.equals(MainActivity.PROV_GPS))
                locationMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            else if (prov.equals(MainActivity.PROV_RED))
                locationMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        } catch (SecurityException e) {

        }



            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        socket = new Socket(IP,PUERTO);
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        out = new PrintWriter(socket.getOutputStream(), true);
                        out.println(C_HOLA);
                        String menServ = in.readLine();
                        if(S_INICIO.equals(menServ)){


                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateLocation();
                                }
                            });


                                out.println(C_UBICACION+":::"+longitud + ":::" + latitud + ":::" + altitud + ":::" + velocidad);
                                menServ = in.readLine();

                            }
                        }
                        in.close();
                        out.close();
                        socket.close();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e){
                        e.printStackTrace();
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };

            t.start();

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
}
