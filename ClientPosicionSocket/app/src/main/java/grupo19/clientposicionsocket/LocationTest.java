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
import android.view.View;
import android.widget.TextView;

public class LocationTest extends AppCompatActivity implements LocationListener {

    private double longitud;
    private double latitud;
    private double velocidad;
    private double altitud;

    private LocationManager locationMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_test);
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUILocation();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

    }

    public void updateUILocation(){
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText(longitud + ";" + latitud + ";" + altitud + ";" + velocidad);
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
