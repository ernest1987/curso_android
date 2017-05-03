package planok.cl.android.pvi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import planok.cl.android.pvi.clases.Propiedades;
import planok.cl.android.pvi.clases.Proyecto;
import planok.cl.android.pvi.database.DatabaseHelper;

public class FormularioEntrega extends Activity {
    private Context context = this;
    int id_proyecto;
    int id_propiedad;
    private DatabaseHelper mDBHelper;
    @BindView(R.id.nombre_proyecto)
    TextView txt_nombre_proyecto;
    @BindView(R.id.nombre_etapa)
    TextView txt_nombre_etapa;
    @BindView(R.id.direccion)
    TextView txt_direccion;
    @BindView(R.id.btn_aceptar_entrega)
    Button btn_aceptar_entrega;
    @BindView(R.id.nombre_propietario)
    EditText edit_nombre_propietario;
    @BindView(R.id.telefono_particular)
    EditText edit_telefono_particular;
    @BindView(R.id.rut_propietario)
    EditText edit_rut_propietario;
    @BindView(R.id.telefono_comercial)
    EditText edit_telefono_comercial;
    @BindView(R.id.email_propietario)
    EditText edit_email_propietario;
    @BindView(R.id.celular_propietario)
    EditText edit_celular_propietario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_entrega);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_proyecto = extras.getInt("id_proyecto");
            id_propiedad = extras.getInt("id_propiedad");
        }
        //Crea la conexion a la base de datos
        mDBHelper = new DatabaseHelper(this);
        Proyecto proyecto = mDBHelper.obtenerDatosProyecto(id_proyecto);
        Propiedades propiedades = mDBHelper.obtenerDatosPropiedades(id_propiedad);
        txt_nombre_proyecto.setText(proyecto.getProyecto_nombre());
        txt_nombre_etapa.setText(proyecto.getProyecto_nombrecorto());
        txt_direccion.setText(propiedades.getPropiedad_direccion());
        /*btn_aceptar_obs_entrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(
                        FormularioEntrega.this, ResumenEntrega.class);
                mainIntent.putExtra("id_proyecto", id_proyecto);
                mainIntent.putExtra("id_propiedad", id_propiedad);
                startActivity(mainIntent);
            }
        });*/
        btn_aceptar_entrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(
                        FormularioEntrega.this, ResumenEntrega.class);
                mainIntent.putExtra("id_proyecto", id_proyecto);
                mainIntent.putExtra("id_propiedad", id_propiedad);
                startActivity(mainIntent);
                finish();
            }
        });
        /*btn_rechazar_entrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogoRechazoEntrega();
            }
        });*/
        //Datos del propietario
        edit_nombre_propietario.setText("Jaime Perez");
        edit_telefono_particular.setText("+5622123456");
        edit_rut_propietario.setText("12.345.678-9");
        edit_telefono_comercial.setText("+5622123457");
        edit_email_propietario.setText("jaimikus@jaimikus.cl");
        edit_celular_propietario.setText("+5697951234");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        /*if (progressDialog.isShowing())
            progressDialog.dismiss();*/
        super.onDestroy();
    }

    @Override
    public void onPause() {
        /*if (progressDialog.isShowing())
            progressDialog.dismiss();*/
        super.onPause();
    }

    private void abrirDialogoRechazoEntrega() {
        //se obtiene la vista de la pantalla de ultima trx
        LayoutInflater li = LayoutInflater.from(context);
        View vista_dialogo = li.inflate(R.layout.dialogo_rechazo_propiedad, null);
        // se crea un contructor del dialogo
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // setea la vista al contructor del dialogo
        alertDialogBuilder.setView(vista_dialogo);
        // Configura el mensaje de dialogo
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                abrirConfirmacionRechazoEntrega();
                            }
                        })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setTitle("Advertencia");
        // Se cera el dialogo
        AlertDialog alertDialog = alertDialogBuilder.create();
        // Se muestra el dialogo
        alertDialog.show();
        //Button button_ok = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //button_ok.setOnClickListener(new CustomListener(alertDialog));
    }

    private void abrirConfirmacionRechazoEntrega() {
        //se obtiene la vista de la pantalla de ultima trx
        LayoutInflater li = LayoutInflater.from(context);
        View vista_dialogo = li.inflate(R.layout.dialogo_confirmacion_rechazo, null);
        // se crea un contructor del dialogo
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // setea la vista al contructor del dialogo
        alertDialogBuilder.setView(vista_dialogo);
        // Configura el mensaje de dialogo
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //.get//txt_monto_en_sesion.setText(monto_total());
                                Intent mainIntent = new Intent(
                                        FormularioEntrega.this, MenuInicial.class);
                                mainIntent.putExtra("id_proyecto", id_proyecto);
                                mainIntent.putExtra("id_propiedad", id_propiedad);
                                startActivity(mainIntent);
                                finish();
                            }
                        })
                .setTitle("Confirmación");
        // Se cera el dialogo
        AlertDialog alertDialog = alertDialogBuilder.create();
        // Se muestra el dialogo
        alertDialog.show();
        //Button button_ok = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //button_ok.setOnClickListener(new CustomListener(alertDialog));
    }
}
