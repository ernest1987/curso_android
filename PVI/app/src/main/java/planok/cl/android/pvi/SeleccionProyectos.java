package planok.cl.android.pvi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import planok.cl.android.pvi.clases.Etapas;
import planok.cl.android.pvi.clases.Propiedades;
import planok.cl.android.pvi.clases.Proyecto;
import planok.cl.android.pvi.clases.Utiles;
import planok.cl.android.pvi.database.DatabaseHelper;

public class SeleccionProyectos extends AppCompatActivity {
    private Context mContext = this;
    //Clase de la base de datos
    private DatabaseHelper mDBHelper;
    Proyecto proyecto_selecionado;
    Etapas etapa_selecionada;
    Propiedades propiedad_selecionada;
    //Listas para los Spinners
    ArrayList<String> lista_proyectos;
    ArrayList<Proyecto> proyectos;
    ArrayList<String> lista_etapas;
    ArrayList<Etapas> etapas;
    ArrayList<String> lista_propiedades;
    ArrayList<Propiedades> propiedades;
    //TAG
    String TAG = "SELECCION DE PROYECTOS";
    //Iniciacion de Vistas
    @BindView(R.id.spinner_seleccion_proyectos)
    Spinner spinner_proyectos;
    @BindView(R.id.spinner_seleccion_etapa)
    Spinner spinner_etapas;
    @BindView(R.id.spinner_seleccion_propiedad)
    Spinner spinner_propieadades;
    @BindView(R.id.ir_menu_inicial)
    Button ingresarButton;
    @BindView(R.id.ir_entrega_terceros)
    Button entregaTerceros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_proyectos);
        ButterKnife.bind(this);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name) + " - Gestión de Entrega");
        //Crea la conexion a la base de datos
        mDBHelper = new DatabaseHelper(this);
        //Revisa si exite la base de datos en la carpeta de la app
        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        //Si la base no existe se copia en la carpeta de las apps
        if (!database.exists()) {
            mDBHelper.getReadableDatabase();
            //Copia la base de datos a la carpeta de la app
            if (copyDatabase(this)) {
                Toast.makeText(this, "Base de datos copiada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al copiar base de datos", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        cargarDataProyectos();
        ingresarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(
                        SeleccionProyectos.this, CheckListEntrega.class);
                if(proyecto_selecionado.getProyecto_id() != 0 && propiedad_selecionada.getPropiedad_id() != 0 && etapa_selecionada.getEtapa_id() != 0){
                    mainIntent.putExtra("proyecto", proyecto_selecionado.getProyecto_nombre());
                    mainIntent.putExtra("id_proyecto", proyecto_selecionado.getProyecto_id());
                    mainIntent.putExtra("propiedad", propiedad_selecionada.getPropiedad_direccion());
                    mainIntent.putExtra("id_propiedad", propiedad_selecionada.getPropiedad_id());
                    mainIntent.putExtra("pos", spinner_propieadades.getSelectedItemPosition());
                    mainIntent.putExtra("tipo_prop", propiedad_selecionada.getTipo_casa_id());
                    startActivity(mainIntent);
                }else{
                    Toast.makeText(SeleccionProyectos.this, "Debe seleccionar todos los datos" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        entregaTerceros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(
                        SeleccionProyectos.this, EntregaTerceros.class);
                if(proyecto_selecionado.getProyecto_id() != 0 && propiedad_selecionada.getPropiedad_id() != 0 && etapa_selecionada.getEtapa_id() != 0){
                    mainIntent.putExtra("proyecto", proyecto_selecionado.getProyecto_nombre());
                    mainIntent.putExtra("id_proyecto", proyecto_selecionado.getProyecto_id());
                    mainIntent.putExtra("propiedad", propiedad_selecionada.getPropiedad_direccion());
                    mainIntent.putExtra("id_propiedad", propiedad_selecionada.getPropiedad_id());
                    mainIntent.putExtra("pos", spinner_propieadades.getSelectedItemPosition());
                    mainIntent.putExtra("tipo_prop", propiedad_selecionada.getTipo_casa_id());
                    startActivity(mainIntent);
                }else{
                    Toast.makeText(SeleccionProyectos.this, "Debe seleccionar todos los datos" , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Funcion para copiar la Base de datos a la carpeta de la app
     */
    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w(TAG, "DB copied");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Funcion para cargar la data de SQLite al spinner
     */
    private void cargarDataProyectos() {
        try {
            //Se obtiene Json desde una funcion que devuelve un String
            String text = Utiles.leer_json_assets("proyectos.json", this);
            //Se obtiene la Data desde el JSON
            JSONObject jsonObj = new JSONObject(text);
            // Getting data JSON Array nodes
            JSONArray data = jsonObj.getJSONArray("proyectos");
            // Elementos del Spinner
            proyectos = new ArrayList<Proyecto>();
            lista_proyectos = new ArrayList<String>();
            //Se agrega data vacia
            proyectos.add(new Proyecto());
            lista_proyectos.add("Seleccionar Proyecto");
            //Se recorre la Data
            Proyecto proyecto = null;
            for (int i = 0; i < data.length(); i++) {
                Log.d("Json proyectos", "Entre al loop");
                JSONObject c = data.getJSONObject(i);
                proyecto = new Proyecto(c.getInt("id_proyecto"), c.getString("nombre"), c.getString("nombrecorto"), c.getString("direccion"));
                proyectos.add(proyecto);
                //Llenar el Arrary List
                lista_proyectos.add(proyecto.getProyecto_nombre());
            }
            // Cargando el adapter para el spinner
            spinner_proyectos
                    .setAdapter(new ArrayAdapter<String>(SeleccionProyectos.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            lista_proyectos));
            // Seleccion del Spinner
            spinner_proyectos
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            // Selecciono el Id de la planta
                            proyecto_selecionado = proyectos.get(position);
                            Log.w(TAG, "ID PROYECTO ---- " + proyecto_selecionado.getProyecto_id());
                            cargarDataPropiedades(proyecto_selecionado.getProyecto_id());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Funcion para cargar la data de SQLite al spinner
     */
    private void cargarDataPropiedades(int id_proyecto) {
        try {
            // Elementos del Spinner
            propiedades = new ArrayList<Propiedades>();
            lista_propiedades = new ArrayList<String>();
            //Se agrega data vacia
            propiedades.add(new Propiedades());
            lista_propiedades.add("Seleccionar Propiedad");
            if(id_proyecto != 0) {
                //Se obtiene Json desde una funcion que devuelve un String
                String text = Utiles.leer_json_assets("proy_" + id_proyecto + ".json", this);
                if (text.equalsIgnoreCase("")) {
                    propiedad_selecionada = null;
                    spinner_propieadades.setAdapter(null);
                    return;
                }
                //Se obtiene la Data desde el JSON
                JSONObject jsonObj = new JSONObject(text);
                // Getting data JSON Array nodes
                JSONArray data = jsonObj.getJSONArray("propiedades");
                //Se recorre la Data para llenar el Spinner
                Propiedades propiedad = null;
                for (int i = 0; i < data.length(); i++) {
                    Log.d(TAG, "Json proyectos - Entre al loop");
                    JSONObject c = data.getJSONObject(i);
                    propiedad = new Propiedades(c.getInt("id_propiedad"), id_proyecto, c.getInt("id_tipo_casa"), c.getString("direccion"), c.getString("etapa"), c.getString("estado"));
                    propiedades.add(propiedad);
                    //Llenar el Arrary List
                    lista_propiedades.add(propiedad.getPropiedad_direccion());
                }
            }
            // Cargando el adapter para el spinner
            spinner_propieadades
                    .setAdapter(new ArrayAdapter<String>(SeleccionProyectos.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            lista_propiedades));
            // Seleccion del Spinner
            spinner_propieadades
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            // Selecciono el Id de la planta
                            propiedad_selecionada = propiedades.get(position);
                            Log.w(TAG, "ID PROPIEDAD ---- " + propiedad_selecionada.getPropiedad_id());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });
            etapas = new ArrayList<Etapas>();
            lista_etapas = new ArrayList<String>();
            //Se agrega data vacia
            etapas.add(new Etapas());
            lista_etapas.add("Seleccionar Etapa");
            Etapas etapa = null;
            for (int i = 1; i < 4; i++) {
                etapa = new Etapas(i, 1, "Etapa "+ i);
                etapas.add(etapa);
                //Llenar el Arrary List
                lista_etapas.add("Etapa "+ i);
            }
            // Cargando el adapter para el spinner
            spinner_etapas
                    .setAdapter(new ArrayAdapter<String>(SeleccionProyectos.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            lista_etapas));
            // Seleccion del Spinner
            spinner_etapas
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            // Selecciono el Id de la planta
                            etapa_selecionada = etapas.get(position);
                            Log.w(TAG, "ID ETAPA ---- " + etapa_selecionada.getEtapa_id());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
