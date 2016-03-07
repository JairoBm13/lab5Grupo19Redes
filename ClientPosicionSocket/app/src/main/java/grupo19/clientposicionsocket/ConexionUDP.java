package grupo19.clientposicionsocket;

import android.content.Context;


import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.location.LocationListener;

import java.net.DatagramSocket;


public class ConexionUDP extends AppCompatActivity implements LocationListener{

    private DatagramSocket socket;
    private final static String SERVIDOR_IP = "192.168.10.38";
    private final static int SERVIDOR_PUERTO = 8080;
    private LocationManager locationMan;

    private double longitud;
    private double latitud;
    private double velocidad;
    private double altitud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_udp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        locationMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        try {
            locationMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }catch(SecurityException e){
            
        }

        try{
            socket = new DatagramSocket();
            while(true){
            }
        }catch(Exception e){

    }

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
