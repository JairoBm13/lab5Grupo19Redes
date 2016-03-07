package grupo19.clientposicionsocket;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConexionTCP extends AppCompatActivity {

    private Socket socket;
    private final static String IP = "192.168.10.38";
    private final static int PUERTO = 8080;

    private PrintWriter out;
    private BufferedReader in;

    LocationManager locationMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_tcp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

}
