package planok.cl.android.pvi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MenuInicial extends AppCompatActivity {
    TextView txt_bienvenida;
    String nombre_usuario = "";
    int id_proyecto;
    String nombre_propiedad = "";
    int id_propiedad;
    Button btn_entregar_propiedad;
    Button btn_sincronizar_datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menu_incial);
        setSupportActionBar(toolbar);
        SharedPreferences prefs = getSharedPreferences("Preferencias_PVI", Context.MODE_PRIVATE);
        nombre_usuario = prefs.getString("nombre_user", "");
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name) + " - " + nombre_usuario);
        txt_bienvenida = (TextView) findViewById(R.id.textBienvenida);
        assert txt_bienvenida != null;
        txt_bienvenida.setText("Hola " + nombre_usuario + ", ¿Qué deseas Hacer?");
        btn_entregar_propiedad = (Button) findViewById(R.id.btn_seleccionar_propiedad);
        btn_entregar_propiedad.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MenuInicial.this, SeleccionProyectos.class);
                startActivity(mainIntent);
            }
        });
        btn_sincronizar_datos = (Button) findViewById(R.id.btn_sincronizar_datos);
        assert btn_sincronizar_datos != null;
        btn_sincronizar_datos.setEnabled(false);
        /*btn_sincronizar_datos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MenuInicial.this, AgregarRequerimento.class);
                mainIntent.putExtra("id_proyecto", id_proyecto);
                mainIntent.putExtra("id_propiedad", id_propiedad);
                startActivity(mainIntent);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_inicial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
