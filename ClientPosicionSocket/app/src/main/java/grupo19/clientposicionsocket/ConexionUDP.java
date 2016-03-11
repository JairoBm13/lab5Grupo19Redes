package grupo19.clientposicionsocket;

import android.content.Context;


import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.location.LocationListener;
import android.widget.TextView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


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

        Intent intent = getIntent();
        String prov = intent.getStringExtra(MainActivity.PROVEEDOR);

        locationMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        try {
            if (prov.equals(MainActivity.PROV_GPS)) locationMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            else if (prov.equals(MainActivity.PROV_RED))locationMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        }catch(SecurityException e){
            
        }

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {

                        Thread.sleep(1000);
                        DatagramSocket udpSocket = new DatagramSocket();
                        String locationData = latitud + ":::" + longitud + ":::" + velocidad + ":::" + altitud;
                        byte[] loccationBytes = locationData.getBytes();
                        InetAddress address = InetAddress.getByName(SERVIDOR_IP);
                        DatagramPacket packet = new DatagramPacket(loccationBytes, locationData.length(),address, SERVIDOR_PUERTO);
                        udpSocket.send(packet);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUILocation();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
                catch(Exception e){

                }
            }
        };

        t.start();

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

}
