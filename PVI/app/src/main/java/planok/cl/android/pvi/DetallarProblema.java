package planok.cl.android.pvi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mvc.imagepicker.ImagePicker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import planok.cl.android.pvi.clases.Problema;
import planok.cl.android.pvi.database.DatabaseHelper;

public class DetallarProblema extends AppCompatActivity {
    private Uri mImageUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_OPEN_RESULT_CODE = 0;
    @BindView(R.id.contendor_problemas)
    LinearLayout contenedor_problemas;
    @BindView(R.id.contendor_fotos)
    LinearLayout contenedor_imagenes;
    String nombre_proyecto = "";
    int id_proyecto;
    String nombre_propiedad = "";
    int id_propiedad;
    int id_recinto;
    int id_lugar;
    String nombre_item = "";
    int id_item;
    private DatabaseHelper mDBHelper;
    HashMap<Integer, String> problemas;
    CheckBox[] ch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_foto);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        ImagePicker.setMinQuality(400, 400);
        if (extras != null) {
            nombre_proyecto = extras.getString("proyecto");
            id_proyecto = extras.getInt("id_proyecto");
            nombre_propiedad = extras.getString("propiedad");
            id_propiedad = extras.getInt("id_propiedad");
            id_recinto = extras.getInt("id_recinto");
            id_lugar = extras.getInt("id_lugar");
            nombre_item = extras.getString("nombre_item");
            id_item = extras.getInt("id_item");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Problemas de " + nombre_item);
        contenedor_imagenes.removeAllViews();
        /*toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button btn_seleccionar_foto = (Button) findViewById(R.id.btn_seleccionar_imagen);
        assert btn_seleccionar_foto != null;
        btn_seleccionar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.pickImage(DetallarProblema.this, "Elegir imagen:");
            }
        });
        Button btn_terminar = (Button) findViewById(R.id.btn_terminar_problemas);
        assert btn_terminar != null;
        btn_terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean seleccionado = false;
                for (int i = 0; i < problemas.size(); i ++) {
                    if (ch[i].isChecked())
                        seleccionado = true;
                }
                if(seleccionado) {
                    finish();
                }else
                    Toast.makeText(DetallarProblema.this, "Debe elejir a lo menos un problema", Toast.LENGTH_SHORT).show();
            }
        });
        mDBHelper = new DatabaseHelper(this);
        cargarProblemas();
    }

    private void cargarProblemas() {
        List<Problema> problema = mDBHelper.listarProblemas(id_recinto, id_lugar, id_item);
        problemas = new HashMap<Integer, String>();
        for (int i = 0; i < problema.size(); i++) {
            Problema probl = problema.get(i);
            if (!probl.getProblema_nombre().equalsIgnoreCase(""))
                problemas.put(probl.getId_problema(), probl.getProblema_nombre());
        }
        contenedor_problemas.removeAllViews();
        Set set = problemas.entrySet();
        Iterator iterator = set.iterator();
        ch = new CheckBox[problemas.size()];
        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            System.out.print("key is: " + mentry.getKey() + " & Value is: ");
            System.out.println(mentry.getValue());
            ch[i] = new CheckBox(DetallarProblema.this);
            ch[i].setId(Integer.parseInt(mentry.getKey().toString()));
            System.out.println("CHID :: " + Integer.parseInt(mentry.getKey().toString()));
            System.out.println("I :: " + i);
            ch[i].setText(mentry.getValue().toString());
            contenedor_problemas.addView(ch[i]);
            i++;
        }
        for (int k = 0; k < problemas.size(); k++) {
            final int j = k;
            ch[j].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    System.out.println("Checked ID :: " + ch[j].getId());
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        ImageView imageview = new ImageView(DetallarProblema.this);
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        imageview.setImageBitmap(getResizedBitmap(bitmap, 200, 300));
        imageview.setLayoutParams(params);
        imageview.setPadding(10,10,10,10);
        //imageview.
       /* Glide.with(this)
                .load(bitmap)
                .into(imageview);*/
        contenedor_imagenes.addView(imageview);
        //foto_tomada.setImageBitmap(bitmap);

        /*if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            foto_tomada.setImageBitmap(imageBitmap);
        }
        if (requestCode == REQUEST_OPEN_RESULT_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                mImageUri = data.getData();
                Glide.with(this)
                        .load(mImageUri)
                        .into(foto_tomada);
            }
        }*/
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

}
