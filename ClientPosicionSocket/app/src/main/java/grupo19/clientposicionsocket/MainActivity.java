package grupo19.clientposicionsocket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.net.Socket;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void tcpConexion(View view){
        Intent intent = new Intent(this, ConexionTCP.class);
        startActivity(intent);
    }

    public void udpConexion(View view){
        Intent intent = new Intent(this, ConexionUDP.class);
        startActivity(intent);
    }
}
