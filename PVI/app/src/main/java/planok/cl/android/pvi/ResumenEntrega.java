package planok.cl.android.pvi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.gcacace.signaturepad.views.SignaturePad;

import butterknife.BindView;
import butterknife.ButterKnife;
import planok.cl.android.pvi.clases.Propiedades;
import planok.cl.android.pvi.clases.Proyecto;
import planok.cl.android.pvi.database.DatabaseHelper;

public class ResumenEntrega extends Activity {
    final Context context = this;
    int id_proyecto;
    int id_propiedad;
    private DatabaseHelper mDBHelper;
    AlertDialog alertDialog;
    String TAG = "RESUMEN ENTREGA";
    @BindView(R.id.nombre_proyecto_r)
    TextView txt_nombre_proyecto;
    @BindView(R.id.nombre_etapa_r)
    TextView txt_nombre_etapa;
    @BindView(R.id.direccion_r)
    TextView txt_direccion;
    @BindView(R.id.firma_propietario_final)
    ImageView firma_propietario_final;
    @BindView(R.id.firma_inmobiliaria_final)
    ImageView firma_inmobiliaria_final;
    @BindView(R.id.btn_aceptar_obs)
    Button btn_aceptar_obs_entrega;
    @BindView(R.id.btn_aceptar_entrega)
    Button btn_aceptar_entrega;
    @BindView(R.id.btn_rechazar_entrega)
    Button btn_rechazar_entrega;
    @BindView(R.id.btn_resumen_checklist)
    Button btn_resumen_checklist;
    @BindView(R.id.btn_datos_recepcion)
    Button btn_datos_recepcion;
    private SignaturePad mSignaturePad;
    Bitmap firmaProietarioBitmap;
    Bitmap firmaInmobiliariaBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_entrega);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_proyecto = extras.getInt("id_proyecto");
            id_propiedad = extras.getInt("id_propiedad");
        }
        //Crea la conexion a la base de datos
        mDBHelper = new DatabaseHelper(context);
        Proyecto proyecto = mDBHelper.obtenerDatosProyecto(id_proyecto);
        Propiedades propiedades = mDBHelper.obtenerDatosPropiedades(id_propiedad);
        txt_nombre_proyecto.setText(proyecto.getProyecto_nombre());
        txt_nombre_etapa.setText(proyecto.getProyecto_nombrecorto());
        txt_direccion.setText(propiedades.getPropiedad_direccion());
        firma_propietario_final.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firma_propietario();
            }
        });
        firma_inmobiliaria_final.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firma_inmobiliaria();
            }
        });

        btn_aceptar_obs_entrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(
                        ResumenEntrega.this, MenuInicial.class);
                mainIntent.putExtra("id_proyecto", id_proyecto);
                mainIntent.putExtra("id_propiedad", id_propiedad);
                startActivity(mainIntent);
                finish();
            }
        });
        btn_aceptar_entrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(
                        ResumenEntrega.this, MenuInicial.class);
                mainIntent.putExtra("id_proyecto", id_proyecto);
                mainIntent.putExtra("id_propiedad", id_propiedad);
                startActivity(mainIntent);
                finish();
            }
        });
        btn_rechazar_entrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogoRechazoEntrega();
            }
        });
        btn_resumen_checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogoResumenChecklist();
            }
        });
        btn_datos_recepcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogoDatosRecepcion();
            }
        });

    }

    private void firma_propietario() {
        //se obtiene la vista de la pantalla de ultima trx
        LayoutInflater li = LayoutInflater.from(context);
        View vista_config = li.inflate(R.layout.dialogo_escribir_firma, null);
        // se crea un contructor del dialogo
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // setea la vista al contructor del dialogo
        alertDialogBuilder.setView(vista_config);

        // setea en input donde el usuario ingresa la firma
        final Button btn_borrar_firma = (Button) vista_config
                .findViewById(R.id.clear_button);
        final Button btn_guardar_firma = (Button) vista_config
                .findViewById(R.id.save_button);
        mSignaturePad = (SignaturePad) vista_config.findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                btn_guardar_firma.setEnabled(true);
                btn_borrar_firma.setEnabled(true);
            }

            @Override
            public void onClear() {
                btn_guardar_firma.setEnabled(false);
                btn_borrar_firma.setEnabled(false);
            }
        });
        btn_borrar_firma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
            }
        });
        btn_guardar_firma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // firma con fondo blanco signatureBitmap = mSignaturePad.getSignatureBitmap();
                firmaProietarioBitmap = mSignaturePad.getTransparentSignatureBitmap();
                if (alertDialog.isShowing())
                    alertDialog.dismiss();
                firma_propietario_final.setImageBitmap(firmaProietarioBitmap);
            }
        });
        // Configura el mensaje de dialogo
        alertDialogBuilder
                .setCancelable(true)
                .setTitle("Firmar Acta");
        // Se cera el dialogo
        alertDialog = alertDialogBuilder.create();
        // Se muestra el dialogo
        alertDialog.show();
    }

    private void firma_inmobiliaria() {
        //se obtiene la vista de la pantalla de ultima trx
        LayoutInflater li = LayoutInflater.from(context);
        View vista_config = li.inflate(R.layout.dialogo_escribir_firma, null);
        // se crea un contructor del dialogo
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // setea la vista al contructor del dialogo
        alertDialogBuilder.setView(vista_config);
        // setea en input donde el usuario ingresa la firma
        final Button btn_borrar_firma = (Button) vista_config
                .findViewById(R.id.clear_button);
        final Button btn_guardar_firma = (Button) vista_config
                .findViewById(R.id.save_button);
        mSignaturePad = (SignaturePad) vista_config.findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                btn_guardar_firma.setEnabled(true);
                btn_borrar_firma.setEnabled(true);
            }

            @Override
            public void onClear() {
                btn_guardar_firma.setEnabled(false);
                btn_borrar_firma.setEnabled(false);
            }
        });
        btn_borrar_firma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
            }
        });
        btn_guardar_firma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // firma con fondo blanco signatureBitmap = mSignaturePad.getSignatureBitmap();
                firmaInmobiliariaBitmap = mSignaturePad.getTransparentSignatureBitmap();
                if (alertDialog.isShowing())
                    alertDialog.dismiss();
                firma_inmobiliaria_final.setImageBitmap(firmaInmobiliariaBitmap);
            }
        });
        // Configura el mensaje de dialogo
        alertDialogBuilder
                .setCancelable(true)
                .setTitle("Firmar Acta");
        // Se crea el dialogo
        alertDialog = alertDialogBuilder.create();
        // Se muestra el dialogo
        alertDialog.show();
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
                                        ResumenEntrega.this, MenuInicial.class);
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

    private void abrirDialogoResumenChecklist() {
        //se obtiene la vista de la pantalla de ultima trx
        LayoutInflater li = LayoutInflater.from(context);
        View vista_dialogo = li.inflate(R.layout.dialogo_resumen_checklist, null);
        // se crea un contructor del dialogo
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // setea la vista al contructor del dialogo
        alertDialogBuilder.setView(vista_dialogo);
        // Configura el mensaje de dialogo
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Volver",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                .setTitle("Resumen Problemas Checklist");
        // Se cera el dialogo
        AlertDialog alertDialog = alertDialogBuilder.create();
        // Se muestra el dialogo
        alertDialog.show();
    }

    private void abrirDialogoDatosRecepcion() {
        //se obtiene la vista de la pantalla de ultima trx
        LayoutInflater li = LayoutInflater.from(context);
        View vista_dialogo = li.inflate(R.layout.dialogo_entrega_terceros, null);
        // se crea un contructor del dialogo
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // setea la vista al contructor del dialogo
        alertDialogBuilder.setView(vista_dialogo);
        // Configura el mensaje de dialogo
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Volver",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                .setTitle("Datos de Recepción");
        // Se cera el dialogo
        AlertDialog alertDialog = alertDialogBuilder.create();
        // Se muestra el dialogo
        alertDialog.show();
    }

    private void grabar_imagen() {
        /*Glide.with(this)
                .load(signatureBitmap)
                .asBitmap().into(firma_propietario_final);
               /* .load(signatureBitmap)
                .into(firma_propietario_final);*/
    }
}
