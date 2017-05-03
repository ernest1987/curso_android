package planok.cl.android.pvi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import planok.cl.android.pvi.adapters.SpinnerAdapterItem;
import planok.cl.android.pvi.adapters.SpinnerAdapterLugares;
import planok.cl.android.pvi.adapters.SpinnerAdapterProblema;
import planok.cl.android.pvi.adapters.SpinnerAdapterRecintos;
import planok.cl.android.pvi.clases.Item;
import planok.cl.android.pvi.clases.Lugar;
import planok.cl.android.pvi.clases.Problema;
import planok.cl.android.pvi.clases.Propiedades;
import planok.cl.android.pvi.clases.Recinto;
import planok.cl.android.pvi.database.DatabaseHelper;

public class AgregarRequerimento extends Activity {
    int id_proyecto;
    int id_propiedad;
    Propiedades propiedad;
    private DatabaseHelper mDBHelper;
    @BindView(R.id.btn_tomar_fotografia)
    Button tomar_foto;
    @BindView(R.id.btn_selecionar_archivo)
    Button selecionar_archivo;
    @BindView(R.id.foto_tomada)
    ImageView foto_tomada;
    @BindView(R.id.spinner_recintos)
    Spinner spinner_recintos;
    @BindView(R.id.spinner_lugar)
    Spinner spinner_lugar;
    @BindView(R.id.spinner_item)
    Spinner spinner_item;
    @BindView(R.id.spinner_problema)
    Spinner spinner_problema;
    private Uri mImageUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_OPEN_RESULT_CODE = 0;
    SpinnerAdapterRecintos dataAdapterRecintos = null;
    SpinnerAdapterLugares dataAdapterLugares = null;
    SpinnerAdapterItem dataAdapterItem = null;
    SpinnerAdapterProblema dataAdapterProblema = null;
    ProgressDialog progressDialog = null;
    Recinto recinto_selecionado;
    Lugar lugar_selecionado;
    Item item_selecionado;
    Problema problema_selecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_requerimiento);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando Base de Conocimiento...");
        progressDialog.show();
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_proyecto = extras.getInt("id_proyecto");
            id_propiedad = extras.getInt("id_propiedad");
        }
        mDBHelper = new DatabaseHelper(this);
        propiedad = mDBHelper.obtenerDatosPropiedades(id_propiedad);
        cargar_data_combos();
        tomar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (tomarFotoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(tomarFotoIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        selecionar_archivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abriArchivoIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                abriArchivoIntent.addCategory(Intent.CATEGORY_OPENABLE);
                abriArchivoIntent.setType("image/*");
                if (abriArchivoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(abriArchivoIntent, REQUEST_OPEN_RESULT_CODE);
                }
            }
        });

        spinner_recintos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                recinto_selecionado = dataAdapterRecintos.getItem(position);
                if (recinto_selecionado.getId_recinto() != 0)
                    cargarDataRecintos(recinto_selecionado.getId_recinto());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
    }

    private void cargar_data_combos() {
        cargarRecintos();
        cargarLugares();
        cargarItem();
        cargarProblema();
        progressDialog.dismiss();
    }

    private void cargarRecintos() {
        // Elementos del Spinner
        List<Recinto> recintos = mDBHelper.listarTodosRecintos(propiedad.getTipo_casa_id());
        // Creando adapter para el spinner
        dataAdapterRecintos = new SpinnerAdapterRecintos(this,
                android.R.layout.simple_spinner_item, recintos);
        // Estilo del Sipnner - Lista de Datos
        dataAdapterRecintos
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // enlazando la informacion al spinner
        spinner_recintos.setAdapter(dataAdapterRecintos);
    }

    private void cargarLugares() {
        // Elementos del Spinner
        List<Lugar> lugar = mDBHelper.listarTodosLugares(propiedad.getTipo_casa_id());
        // Creando adapter para el spinner
        dataAdapterLugares = new SpinnerAdapterLugares(this,
                android.R.layout.simple_spinner_item, lugar);
        // Estilo del Sipnner - Lista de Datos
        dataAdapterLugares
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // enlazando la informacion al spinner
        spinner_lugar.setAdapter(dataAdapterLugares);
    }

    private void cargarItem() {
        // Elementos del Spinner
        List<Item> item = mDBHelper.listarTodosItems(propiedad.getTipo_casa_id());
        // Creando adapter para el spinner
        dataAdapterItem = new SpinnerAdapterItem(this,
                android.R.layout.simple_spinner_item, item);
        // Estilo del Sipnner - Lista de Datos
        dataAdapterItem
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // enlazando la informacion al spinner
        spinner_item.setAdapter(dataAdapterItem);
    }

    private void cargarProblema() {
        // Elementos del Spinner
        List<Problema> problema = mDBHelper.listarTodosProblemas(propiedad.getTipo_casa_id());
        // Creando adapter para el spinner
        dataAdapterProblema = new SpinnerAdapterProblema(this,
                android.R.layout.simple_spinner_item, problema);
        // Estilo del Sipnner - Lista de Datos
        dataAdapterProblema
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // enlazando la informacion al spinner
        spinner_problema.setAdapter(dataAdapterProblema);
    }

    private void cargarDataRecintos(int id_recinto) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog.setMessage("Cargando Filtros...");
        progressDialog.show();
        //Se cargan los Lugares
        List<Lugar> lugar = mDBHelper.listarLugaresRecintos(propiedad.getTipo_casa_id(), id_recinto);
        dataAdapterLugares = new SpinnerAdapterLugares(this, android.R.layout.simple_spinner_item, lugar);
        dataAdapterLugares.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lugar.setAdapter(dataAdapterLugares);
        //Se cargan los Items
        List<Item> item = mDBHelper.listarItemsRecintos(propiedad.getTipo_casa_id(), id_recinto);
        dataAdapterItem = new SpinnerAdapterItem(this, android.R.layout.simple_spinner_item, item);
        dataAdapterItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_item.setAdapter(dataAdapterItem);
        //Se cargan los Problema
        List<Problema> problema = mDBHelper.listarProblemasRecintos(propiedad.getTipo_casa_id(), id_recinto);
        dataAdapterProblema = new SpinnerAdapterProblema(this, android.R.layout.simple_spinner_item, problema);
        dataAdapterProblema.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_problema.setAdapter(dataAdapterProblema);
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Glide.with(this)
                    .load(imageBitmap)
                    .into(foto_tomada);
            //foto_tomada.setImageBitmap(imageBitmap);
        }
        if (requestCode == REQUEST_OPEN_RESULT_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                mImageUri = data.getData();
                Glide.with(this)
                        .load(mImageUri)
                        .into(foto_tomada);
            }
        }
    }

}
