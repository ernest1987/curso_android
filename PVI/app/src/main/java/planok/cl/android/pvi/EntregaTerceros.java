package planok.cl.android.pvi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mvc.imagepicker.ImagePicker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntregaTerceros extends AppCompatActivity {
    @BindView(R.id.contendor_fotos)
    LinearLayout contenedor_imagenes;
    @BindView(R.id.btn_cedula_identidad)
    ImageButton btn_cedula;
    @BindView(R.id.btn_poder_simple)
    ImageButton btn_poder_simple;
    @BindView(R.id.btn_tarjeta_identificacion)
    ImageButton btn_tarjeta_identificacion;
    @BindView(R.id.btn_fotocopia_cedula)
    ImageButton btn_fotocopia_cedula;
    @BindView(R.id.ir_entrega_terceros)
    Button btn_entrega_terceros;
    // 1 = Foto Cedula
    // 2 = Foto Tarjeta Identificacion
    // 3 = Foto Poder
    // 4 = Foto Cedula Propientario
    int indicador_foto = 0;
    //Datos propiedad
    String nombre_proyecto = "";
    int id_proyecto;
    String nombre_propiedad = "";
    int id_tipo_prop;
    int id_propiedad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega_terceros);
        ButterKnife.bind(this);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name) + " - Entrega a Terceros");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImagePicker.setMinQuality(400, 400);
        contenedor_imagenes.removeAllViews();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nombre_proyecto = extras.getString("proyecto");
            id_proyecto = extras.getInt("id_proyecto");
            nombre_propiedad = extras.getString("propiedad");
            id_propiedad = extras.getInt("id_propiedad");
            id_tipo_prop = extras.getInt("tipo_prop");
        }
        btn_cedula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.pickImage(EntregaTerceros.this, "Elegir imagen:");
                indicador_foto = 1;
            }
        });
        btn_tarjeta_identificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.pickImage(EntregaTerceros.this, "Elegir imagen:");
                indicador_foto = 2;
            }
        });
        btn_poder_simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.pickImage(EntregaTerceros.this, "Elegir imagen:");
                indicador_foto = 3;
            }
        });
        btn_fotocopia_cedula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.pickImage(EntregaTerceros.this, "Elegir imagen:");
                indicador_foto = 4;
            }
        });

        btn_entrega_terceros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(
                        EntregaTerceros.this, CheckListEntrega.class);
                if(id_proyecto != 0 && id_propiedad != 0){
                    mainIntent.putExtra("proyecto", nombre_proyecto);
                    mainIntent.putExtra("id_proyecto", id_proyecto);
                    mainIntent.putExtra("propiedad", nombre_propiedad);
                    mainIntent.putExtra("id_propiedad", id_propiedad);
                    mainIntent.putExtra("tipo_prop",id_tipo_prop);
                    startActivity(mainIntent);
                }else{
                    Toast.makeText(EntregaTerceros.this, "Debe seleccionar todos los datos" , Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        ImageView imageview = new ImageView(EntregaTerceros.this);
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageview.setImageBitmap(getResizedBitmap(bitmap, 200, 300));
        imageview.setLayoutParams(params);
        imageview.setPadding(10, 10, 10, 10);
        //imageview.
       /* Glide.with(this)
                .load(bitmap)
                .into(imageview);*/
        contenedor_imagenes.addView(imageview);
        switch (indicador_foto) {
            case 1:
                btn_cedula.setImageResource(R.drawable.camera_green);
                break;
            case 2:
                btn_tarjeta_identificacion.setImageResource(R.drawable.camera_green);
                break;
            case 3:
                btn_poder_simple.setImageResource(R.drawable.camera_green);
                break;
            case 4:
                btn_fotocopia_cedula.setImageResource(R.drawable.camera_green);
                break;
            default:
                btn_cedula.setImageResource(R.drawable.camera_des_gray);
                btn_tarjeta_identificacion.setImageResource(R.drawable.camera_des_gray);
                btn_poder_simple.setImageResource(R.drawable.camera_des_gray);
                btn_fotocopia_cedula.setImageResource(R.drawable.camera_des_gray);
                break;
        }
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
