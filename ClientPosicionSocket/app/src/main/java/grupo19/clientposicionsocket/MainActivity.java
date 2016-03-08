package grupo19.clientposicionsocket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.net.Socket;

public class MainActivity extends Activity {

    public final static String PROVEEDOR = "PROVEEDOR";
    public final static String PROV_GPS = "Usar GPS";
    public final static String PROV_RED = "Usar red";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void tcpConexion(View view){
        Intent intent = new Intent(this, ConexionTCP.class);
        intent.putExtra(PROVEEDOR, getSelectedRadio());
        startActivity(intent);
    }

    public void udpConexion(View view){
        Intent intent = new Intent(this, ConexionUDP.class);
        intent.putExtra(PROVEEDOR, getSelectedRadio());
        startActivity(intent);
    }

    public void test(View view){
        Intent intent = new Intent(this, LocationTest.class);
        intent.putExtra(PROVEEDOR, getSelectedRadio());
        startActivity(intent);
    }

    public String getSelectedRadio(){
        RadioGroup radioProveedor = (RadioGroup) findViewById(R.id.radioProveedor);
        int selectedId = radioProveedor.getCheckedRadioButtonId();
        RadioButton radBut = (RadioButton) findViewById(selectedId);
        return radBut.getText().toString();
    }
}
