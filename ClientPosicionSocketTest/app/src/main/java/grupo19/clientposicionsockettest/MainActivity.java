package grupo19.clientposicionsockettest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
        intent.putExtra("IP", getIP());
        intent.putExtra("PORT", getPort());
        intent.putExtra("HILOS",getHilos());
        intent.putExtra("RAMP",getRampUp());
        intent.putExtra("CICLOS",getCiclos());
        startActivity(intent);
    }

    public void udpConexion(View view) {
        Intent intent = new Intent(this, ConexionUDP.class);
        intent.putExtra(PROVEEDOR, getSelectedRadio());
        intent.putExtra("IP", getIP());
        intent.putExtra("PORT", getPort());
        intent.putExtra("HILOS",getHilos());
        intent.putExtra("RAMP",getRampUp());
        intent.putExtra("CICLOS",getCiclos());
        startActivity(intent);
    }

    public String getSelectedRadio(){
        RadioGroup radioProveedor = (RadioGroup) findViewById(R.id.radioProveedor);
        int selectedId = radioProveedor.getCheckedRadioButtonId();
        RadioButton radBut = (RadioButton) findViewById(selectedId);
        return radBut.getText().toString();
    }

    public String getIP(){
        EditText editable = (EditText) findViewById(R.id.IPText);
        return editable.getText().toString();
    }

    public String getPort(){
        EditText editable = (EditText) findViewById(R.id.puertoText);
        return editable.getText().toString();
    }

    public String getHilos(){
        EditText editable = (EditText) findViewById(R.id.hilosText);
        return editable.getText().toString();
    }

    public String getRampUp(){
        EditText editable = (EditText)findViewById(R.id.rampText);
        return editable.getText().toString();
    }

    public String getCiclos(){
        EditText editable = (EditText)findViewById(R.id.ciclosText);
        return editable.getText().toString();
    }
}
