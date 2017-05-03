package planok.cl.android.pvi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.viewbadger.BadgeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import planok.cl.android.pvi.adapters.SpinnerAdapterItem;
import planok.cl.android.pvi.adapters.SpinnerAdapterLugares;
import planok.cl.android.pvi.adapters.SpinnerAdapterRecintos;
import planok.cl.android.pvi.clases.Item;
import planok.cl.android.pvi.clases.Lugar;
import planok.cl.android.pvi.clases.Recinto;
import planok.cl.android.pvi.clases.Utiles;
import planok.cl.android.pvi.database.DatabaseHelper;

public class CheckListEntrega extends Activity {
    private Context context = this;
    String nombre_proyecto = "";
    int id_proyecto;
    String nombre_propiedad = "";
    int id_tipo_prop;
    int id_propiedad;
    int id_checklist;
    private ArrayList<Recinto> lista_recintos;
    private ArrayList<Lugar> lista_lugar;
    private ArrayList<Item> lista_item;//TAG
    String TAG = "SELECCION DE PROYECTOS";
    boolean isFirstViewClick = false;
    boolean isSecondViewClick = false;
    @BindView(R.id.Datos_depto)
    TextView txt_datos_depto;
    @BindView(R.id.entregar_propiedad)
    Button btn_entregar_propiedad;
    @BindView(R.id.agregar_recinto)
    Button btn_agregar_recinto;
    @BindView(R.id.linear_ListView)
    LinearLayout mContenedorListView;
    ProgressDialog progressDialog = null;
    private DatabaseHelper mDBHelper;
    private ArrayList<Integer> recintos_selecionados;
    SpinnerAdapterRecintos dataAdapterRecintos = null;
    SpinnerAdapterLugares dataAdapterLugares = null;
    SpinnerAdapterItem dataAdapterItem = null;
    Recinto recinto_selecionado_combo;
    Lugar lugar_selecionado_combo;
    Item item_selecionado_combo;
    int pos_recinto_agregar;
    int pos_lugar_agregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list_entrega);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando Checklist...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Bundle extras = getIntent().getExtras();
        recintos_selecionados = new ArrayList<>();
        if (extras != null) {
            nombre_proyecto = extras.getString("proyecto");
            id_proyecto = extras.getInt("id_proyecto");
            nombre_propiedad = extras.getString("propiedad");
            id_propiedad = extras.getInt("id_propiedad");
            id_tipo_prop = extras.getInt("tipo_prop");
        }
        mDBHelper = new DatabaseHelper(this);
        txt_datos_depto.setText("CHECKLIST ENTREGA - " + nombre_propiedad);
        btn_entregar_propiedad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(CheckListEntrega.this, FormularioEntrega.class);
                mainIntent.putExtra("id_proyecto", id_proyecto);
                mainIntent.putExtra("id_propiedad", id_propiedad);
                startActivity(mainIntent);
                finish();
            }
        });
        cargarDataChecklist();
        btn_agregar_recinto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogoRecinto();
            }
        });
    }

    private void cargarDataChecklist() {
        try {
            //Se obtiene Json desde una funcion que devuelve un String
            String text = Utiles.leer_json_assets("checklist_1.json", this);
            if (text.equalsIgnoreCase("")) {
                Toast.makeText(this, "Error al cargar CheckList", Toast.LENGTH_SHORT).show();
                return;
            }
            lista_recintos = new ArrayList<Recinto>();
            //Se obtiene la Data desde el JSON
            JSONObject jsonObj = new JSONObject(text);
            id_checklist = jsonObj.getInt("id_checklist");
            // Getting data JSON Array nodes
            JSONArray data = jsonObj.getJSONArray("recintos");
            for (int i = 0; i < data.length(); i++) {
                Log.d(TAG, "Json proyectos - Entre al loop");
                //Se extraen los recintos
                JSONObject recintos_loop = data.getJSONObject(i);
                int id_recinto = recintos_loop.getInt("id_recinto");
                String nombre_recinto = recintos_loop.getString("nombre_recinto");
                Log.d(TAG, "<-- Loop Recinto --> " + i);
                Log.d(TAG, "Id Recinto --> " + id_recinto);
                Log.d(TAG, "Nombre Recinto --> " + nombre_recinto);
                //Se crea un array para poblar los Lugares
                ArrayList<Lugar> lista_lugar = new ArrayList<Lugar>();
                //Se extrae la informacion de los lugares y se recorren con el for
                JSONArray data_lugar = recintos_loop.getJSONArray("lugar");
                for (int j = 0; j < data_lugar.length(); j++) {
                    //Se extraen los lugares
                    JSONObject lugar_loop = data_lugar.getJSONObject(j);
                    int id_lugar = lugar_loop.getInt("id_lugar");
                    String nombre_lugar = lugar_loop.getString("nombre_lugar");
                    Log.d(TAG, "<-- Loop Lugar --> " + j);
                    Log.d(TAG, "Id Lugar --> " + id_lugar);
                    Log.d(TAG, "Nombre Lugar  --> " + nombre_lugar);
                    //Se crea un array para poblar los Items
                    ArrayList<Item> lista_item = new ArrayList<Item>();
                    //Se extrae la informacion de los lugares y se recorren con el for
                    JSONArray data_item = lugar_loop.getJSONArray("items");
                    for (int k = 0; k < data_item.length(); k++) {
                        //Se extraen los items
                        JSONObject item_loop = data_item.getJSONObject(k);
                        int id_item = item_loop.getInt("id_item");
                        String nombre_item = item_loop.getString("nombre_item");
                        Log.d(TAG, "<-- Loop Item --> " + k);
                        Log.d(TAG, "Id Item --> " + id_item);
                        Log.d(TAG, "Nombre Item  --> " + nombre_item);
                        lista_item.add(new Item(id_item, nombre_item));
                    }
                    lista_lugar.add(new Lugar(id_lugar, nombre_lugar, lista_item));
                }
                lista_recintos.add(new Recinto(id_recinto, nombre_recinto, lista_lugar));
            }
            cargarVistaCheckList();
        } catch (Exception e) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar CheckList", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarVistaCheckList() {
        try {
            //Adds data into first row
            for (int i = 0; i < lista_recintos.size(); i++) {
                LayoutInflater inflater = null;
                inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mListaRecinto = inflater.inflate(R.layout.cabecera_lista_recinto, null);
                final TextView mRecintoNombre = (TextView) mListaRecinto.findViewById(R.id.nombre_recinto);
                final RelativeLayout mLinearRecinto = (RelativeLayout) mListaRecinto.findViewById(R.id.linearRecinto);
                final ImageView mImgFlechaRecinto = (ImageView) mListaRecinto.findViewById(R.id.imgflechaRecinto);
                final LinearLayout mLinearDetalleLugar = (LinearLayout) mListaRecinto.findViewById(R.id.linear_scroll);
                final ImageButton mBtnAgregarLugar = (ImageButton) mListaRecinto.findViewById(R.id.btn_agregar_lugar);
                final ImageButton mBtnCumpleTodo = (ImageButton) mListaRecinto.findViewById(R.id.btn_cumple_todo);
                final int pos_recinto = i;
                //checkes if menu is already opened or not
                if (!isFirstViewClick) {
                    mLinearDetalleLugar.setVisibility(View.GONE);
                    mImgFlechaRecinto.setBackgroundResource(R.drawable.chevron_circle_right_red);
                } else {
                    mLinearDetalleLugar.setVisibility(View.VISIBLE);
                    mImgFlechaRecinto.setBackgroundResource(R.drawable.chevron_circle_down_red);
                }
                //Handles onclick effect on list item
                mLinearRecinto.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (!isFirstViewClick) {
                            isFirstViewClick = true;
                            mImgFlechaRecinto.setBackgroundResource(R.drawable.chevron_circle_down_red);
                            mLinearDetalleLugar.setVisibility(View.VISIBLE);
                        } else {
                            isFirstViewClick = false;
                            mImgFlechaRecinto.setBackgroundResource(R.drawable.chevron_circle_right_red);
                            mLinearDetalleLugar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                });
                mBtnAgregarLugar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context, "Agregar Lugar del Recinto : " + lista_recintos.get(pos_recinto).getId_recinto(), Toast.LENGTH_SHORT).show();
                        abrirDialogoLugar(lista_recintos.get(pos_recinto).getId_recinto(), lista_recintos.get(pos_recinto).getRecinto_nombre(), pos_recinto);
                    }
                });
                mBtnCumpleTodo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recintos_selecionados.add(lista_recintos.get(pos_recinto).getId_recinto());
                        recargarVistaCheckList();
                    }
                });
                final String recintoNombre = lista_recintos.get(i).getRecinto_nombre();
                mRecintoNombre.setText(recintoNombre);

                //Adds data into second row
                for (int j = 0; j < lista_recintos.get(i).getmLugarList().size(); j++) {
                    LayoutInflater inflater2 = null;
                    inflater2 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View mListaLugar = inflater2.inflate(R.layout.cabecera_lista_lugar, null);
                    TextView mLugarNombre = (TextView) mListaLugar.findViewById(R.id.nombre_lugar);
                    final RelativeLayout mLinearLugar = (RelativeLayout) mListaLugar.findViewById(R.id.linearLugar);
                    final ImageView mImgFlechaLugar = (ImageView) mListaLugar.findViewById(R.id.imgflechaLugar);
                    final LinearLayout mLinearDetalleItem = (LinearLayout) mListaLugar.findViewById(R.id.linear_scroll_third);
                    final ImageButton mBtnAgregarItem = (ImageButton) mListaLugar.findViewById(R.id.btn_agregar_item);
                    final int pos_lugar = j;
                    //checkes if menu is already opened or not
                    if (!isSecondViewClick) {
                        mLinearDetalleItem.setVisibility(View.GONE);
                        mImgFlechaLugar.setBackgroundResource(R.drawable.chevron_circle_right_green);
                    } else {
                        mLinearDetalleItem.setVisibility(View.VISIBLE);
                        mImgFlechaLugar.setBackgroundResource(R.drawable.chevron_circle_down_green);
                    }
                    //Handles onclick effect on list item
                    mLinearLugar.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (!isSecondViewClick) {
                                isSecondViewClick = true;
                                mImgFlechaLugar.setBackgroundResource(R.drawable.chevron_circle_down_green);
                                mLinearDetalleItem.setVisibility(View.VISIBLE);

                            } else {
                                isSecondViewClick = false;
                                mImgFlechaLugar.setBackgroundResource(R.drawable.chevron_circle_right_green);
                                mLinearDetalleItem.setVisibility(View.GONE);
                            }
                            return false;
                        }
                    });
                    mBtnAgregarItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, "Agregar Item del Lugar : " + lista_recintos.get(pos_recinto).getmLugarList().get(pos_lugar).getId_lugar(), Toast.LENGTH_SHORT).show();
                            abrirDialogoItem(lista_recintos.get(pos_recinto).getId_recinto(), lista_recintos.get(pos_recinto).getRecinto_nombre(), pos_recinto, lista_recintos.get(pos_recinto).getmLugarList().get(pos_lugar).getId_lugar(), lista_recintos.get(pos_recinto).getmLugarList().get(pos_lugar).getLugar_nombre(), pos_lugar);
                        }
                    });

                    final String lugarNombre = lista_recintos.get(i).getmLugarList().get(j).getLugar_nombre();
                    mLugarNombre.setText(lugarNombre);
                    //Adds items in subcategories
                    for (int k = 0; k < lista_recintos.get(i).getmLugarList().get(j).getmItemList().size(); k++) {
                        LayoutInflater inflater3 = null;
                        inflater3 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View mListaItem = inflater3.inflate(R.layout.detalle_lista_item, null);
                        TextView mItemName = (TextView) mListaItem.findViewById(R.id.nombre_det_item);
                        final ImageButton mBtnCumpleItem = (ImageButton) mListaItem.findViewById(R.id.check_det_cumple);
                        final ImageButton mBtnFallaItem = (ImageButton) mListaItem.findViewById(R.id.check_det_falla);
                        View target = mListaItem.findViewById(R.id.check_det_falla);
                        BadgeView badge = new BadgeView(this, target);
                        badge.setText("1");
                        //badge.setTextColor(Color.BLUE);
                        //badge.setBadgeBackgroundColor(Color.YELLOW);
                        badge.show();
                        //final ImageButton mBtnIngresarComentario = (ImageButton) mListaItem.findViewById(R.id.btn_ingresar_comentario);
                        //final ImageButton mBtnIngresarFoto = (ImageButton) mListaItem.findViewById(R.id.btn_ingresar_foto);
                        final int pos_item = k;
                        final String itemName = lista_recintos.get(i).getmLugarList().get(j).getmItemList().get(k).getItem_nombre();
                        mItemName.setText(itemName);
                        mBtnCumpleItem.setTag(R.drawable.cumple_des_green);
                        mBtnFallaItem.setTag(R.drawable.no_cumple_des_red);
                        mBtnCumpleItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mBtnCumpleItem.getTag().toString().equalsIgnoreCase(R.drawable.cumple_des_green + "")) {
                                    mBtnCumpleItem.setImageResource(R.drawable.cumple_green);
                                    mBtnCumpleItem.setTag(R.drawable.cumple_green);
                                } else {
                                    mBtnCumpleItem.setImageResource(R.drawable.cumple_des_green);
                                    mBtnCumpleItem.setTag(R.drawable.cumple_des_green);
                                }
                                mBtnFallaItem.setImageResource(R.drawable.no_cumple_des_red);
                            }
                        });
                        mBtnFallaItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mBtnCumpleItem.setImageResource(R.drawable.cumple_des_green);
                                if (mBtnFallaItem.getTag().toString().equalsIgnoreCase(R.drawable.no_cumple_des_red + "")) {
                                    mBtnFallaItem.setImageResource(R.drawable.no_cumple_red);
                                    mBtnFallaItem.setTag(R.drawable.no_cumple_red);
                                    Intent mainIntent = new Intent().setClass(
                                            CheckListEntrega.this, DetallarProblema.class);
                                    mainIntent.putExtra("proyecto", nombre_proyecto);
                                    mainIntent.putExtra("id_proyecto", id_proyecto);
                                    mainIntent.putExtra("id_propiedad", id_propiedad);
                                    mainIntent.putExtra("id_recinto", lista_recintos.get(pos_recinto).getId_recinto());
                                    mainIntent.putExtra("id_lugar", lista_recintos.get(pos_recinto).getmLugarList().get(pos_lugar).getId_lugar());
                                    mainIntent.putExtra("nombre_item", lista_recintos.get(pos_recinto).getmLugarList().get(pos_lugar).getmItemList().get(pos_item).getItem_nombre());
                                    mainIntent.putExtra("id_item", lista_recintos.get(pos_recinto).getmLugarList().get(pos_lugar).getmItemList().get(pos_item).getId_item());

                                    //mainIntent.putExtra("pos", spinner_propieadades.getSelectedItemPosition());
                                    startActivity(mainIntent);
                                } else {
                                    mBtnFallaItem.setImageResource(R.drawable.no_cumple_des_red);
                                    mBtnFallaItem.setTag(R.drawable.no_cumple_des_red);
                                }

                            }
                        });
                        mLinearDetalleItem.addView(mListaItem);
                    }
                    mLinearDetalleLugar.addView(mListaLugar);
                }
                mContenedorListView.addView(mListaRecinto);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar CheckList", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    private void recargarVistaCheckList() {
        progressDialog.show();
        mContenedorListView.removeAllViews();
        try {
            //Adds data into first row
            for (int i = 0; i < lista_recintos.size(); i++) {
                LayoutInflater inflater = null;
                inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mListaRecinto = inflater.inflate(R.layout.cabecera_lista_recinto, null);
                final TextView mRecintoNombre = (TextView) mListaRecinto.findViewById(R.id.nombre_recinto);
                final RelativeLayout mLinearRecinto = (RelativeLayout) mListaRecinto.findViewById(R.id.linearRecinto);
                final ImageView mImgFlechaRecinto = (ImageView) mListaRecinto.findViewById(R.id.imgflechaRecinto);
                final LinearLayout mLinearDetalleLugar = (LinearLayout) mListaRecinto.findViewById(R.id.linear_scroll);
                final ImageButton mBtnAgregarLugar = (ImageButton) mListaRecinto.findViewById(R.id.btn_agregar_lugar);
                final ImageButton mBtnCumpleTodo = (ImageButton) mListaRecinto.findViewById(R.id.btn_cumple_todo);
                final int pos_recinto = i;
                //checkes if menu is already opened or not
                if (recintos_selecionados.contains(lista_recintos.get(i).getId_recinto())) {
                    mBtnCumpleTodo.setTag(R.drawable.cumple_green);
                    mBtnCumpleTodo.setImageResource(R.drawable.cumple_green);
                } else {
                    mBtnCumpleTodo.setTag(R.drawable.cumple_todo_des_red);
                    mBtnCumpleTodo.setImageResource(R.drawable.cumple_todo_des_red);
                }
                if (!isFirstViewClick) {
                    mLinearDetalleLugar.setVisibility(View.GONE);
                    mImgFlechaRecinto.setBackgroundResource(R.drawable.chevron_circle_right_red);
                } else {
                    mLinearDetalleLugar.setVisibility(View.VISIBLE);
                    mImgFlechaRecinto.setBackgroundResource(R.drawable.chevron_circle_down_red);
                }
                //Handles onclick effect on list item
                mLinearRecinto.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (!isFirstViewClick) {
                            isFirstViewClick = true;
                            mImgFlechaRecinto.setBackgroundResource(R.drawable.chevron_circle_down_red);
                            mLinearDetalleLugar.setVisibility(View.VISIBLE);
                        } else {
                            isFirstViewClick = false;
                            mImgFlechaRecinto.setBackgroundResource(R.drawable.chevron_circle_right_red);
                            mLinearDetalleLugar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                });
                mBtnAgregarLugar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Agregar Lugar del Recinto : " + lista_recintos.get(pos_recinto).getId_recinto(), Toast.LENGTH_SHORT).show();
                        abrirDialogoLugar(lista_recintos.get(pos_recinto).getId_recinto(), lista_recintos.get(pos_recinto).getRecinto_nombre(), pos_recinto);
                    }
                });
                mBtnCumpleTodo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mBtnCumpleTodo.getTag().toString().equalsIgnoreCase(R.drawable.cumple_green + "")) {
                            for (int i = 0; i < recintos_selecionados.size(); i++) {
                                if (recintos_selecionados.get(i) == lista_recintos.get(pos_recinto).getId_recinto())
                                    recintos_selecionados.remove(i);
                            }
                            recargarVistaCheckList();
                        } else {
                            recintos_selecionados.add(lista_recintos.get(pos_recinto).getId_recinto());
                            recargarVistaCheckList();
                        }
                    }
                });
                final String recintoNombre = lista_recintos.get(i).getRecinto_nombre();
                mRecintoNombre.setText(recintoNombre);

                //Adds data into second row
                for (int j = 0; j < lista_recintos.get(i).getmLugarList().size(); j++) {
                    LayoutInflater inflater2 = null;
                    inflater2 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View mListaLugar = inflater2.inflate(R.layout.cabecera_lista_lugar, null);
                    TextView mLugarNombre = (TextView) mListaLugar.findViewById(R.id.nombre_lugar);
                    final RelativeLayout mLinearLugar = (RelativeLayout) mListaLugar.findViewById(R.id.linearLugar);
                    final ImageView mImgFlechaLugar = (ImageView) mListaLugar.findViewById(R.id.imgflechaLugar);
                    final LinearLayout mLinearDetalleItem = (LinearLayout) mListaLugar.findViewById(R.id.linear_scroll_third);
                    final ImageButton mBtnAgregarItem = (ImageButton) mListaLugar.findViewById(R.id.btn_agregar_item);
                    final int pos_lugar = j;
                    //checkes if menu is already opened or not
                    if (!isSecondViewClick) {
                        mLinearDetalleItem.setVisibility(View.GONE);
                        mImgFlechaLugar.setBackgroundResource(R.drawable.chevron_circle_right_green);
                    } else {
                        mLinearDetalleItem.setVisibility(View.VISIBLE);
                        mImgFlechaLugar.setBackgroundResource(R.drawable.chevron_circle_down_green);
                    }
                    //Handles onclick effect on list item
                    mLinearLugar.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (!isSecondViewClick) {
                                isSecondViewClick = true;
                                mImgFlechaLugar.setBackgroundResource(R.drawable.chevron_circle_down_green);
                                mLinearDetalleItem.setVisibility(View.VISIBLE);

                            } else {
                                isSecondViewClick = false;
                                mImgFlechaLugar.setBackgroundResource(R.drawable.chevron_circle_right_green);
                                mLinearDetalleItem.setVisibility(View.GONE);
                            }
                            return false;
                        }
                    });
                    mBtnAgregarItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "Agregar Item del Lugar : " + lista_recintos.get(pos_recinto).getmLugarList().get(pos_lugar).getId_lugar(), Toast.LENGTH_SHORT).show();
                            abrirDialogoItem(lista_recintos.get(pos_recinto).getId_recinto(), lista_recintos.get(pos_recinto).getRecinto_nombre(), pos_recinto, lista_recintos.get(pos_recinto).getmLugarList().get(pos_lugar).getId_lugar(), lista_recintos.get(pos_recinto).getmLugarList().get(pos_lugar).getLugar_nombre(), pos_lugar);
                        }
                    });

                    final String lugarNombre = lista_recintos.get(i).getmLugarList().get(j).getLugar_nombre();
                    mLugarNombre.setText(lugarNombre);
                    //Adds items in subcategories
                    for (int k = 0; k < lista_recintos.get(i).getmLugarList().get(j).getmItemList().size(); k++) {
                        LayoutInflater inflater3 = null;
                        inflater3 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View mListaItem = inflater3.inflate(R.layout.detalle_lista_item, null);
                        TextView mItemName = (TextView) mListaItem.findViewById(R.id.nombre_det_item);
                        final ImageButton mBtnCumpleItem = (ImageButton) mListaItem.findViewById(R.id.check_det_cumple);
                        final ImageButton mBtnFallaItem = (ImageButton) mListaItem.findViewById(R.id.check_det_falla);
                        //View target = mListaItem.findViewById(R.id.check_det_falla);
                        BadgeView badge = new BadgeView(this, mBtnFallaItem);
                        badge.setText("1");
                        badge.show();
                        //final ImageButton mBtnIngresarComentario = (ImageButton) mListaItem.findViewById(R.id.btn_ingresar_comentario);
                        //final ImageButton mBtnIngresarFoto = (ImageButton) mListaItem.findViewById(R.id.btn_ingresar_foto);
                        final int pos_item = k;
                        final String itemName = lista_recintos.get(i).getmLugarList().get(j).getmItemList().get(k).getItem_nombre();
                        mItemName.setText(itemName);
                        if (recintos_selecionados.contains(lista_recintos.get(i).getId_recinto())) {
                            mBtnCumpleItem.setTag(R.drawable.cumple_green);
                            mBtnCumpleItem.setImageResource(R.drawable.cumple_green);
                        } else {
                            mBtnCumpleItem.setTag(R.drawable.cumple_des_green);
                            mBtnCumpleItem.setImageResource(R.drawable.cumple_des_green);
                        }
                        mBtnFallaItem.setTag(R.drawable.no_cumple_des_red);
                        mBtnCumpleItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mBtnCumpleItem.getTag().toString().equalsIgnoreCase(R.drawable.cumple_des_green + "")) {
                                    mBtnCumpleItem.setImageResource(R.drawable.cumple_green);
                                    mBtnCumpleItem.setTag(R.drawable.cumple_green);
                                } else {
                                    mBtnCumpleItem.setImageResource(R.drawable.cumple_des_green);
                                    mBtnCumpleItem.setTag(R.drawable.cumple_des_green);
                                }
                                mBtnFallaItem.setImageResource(R.drawable.no_cumple_des_red);
                            }
                        });
                        mBtnFallaItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mBtnCumpleItem.setImageResource(R.drawable.cumple_des_green);
                                if (mBtnFallaItem.getTag().toString().equalsIgnoreCase(R.drawable.no_cumple_des_red + "")) {
                                    mBtnFallaItem.setImageResource(R.drawable.no_cumple_red);
                                    mBtnFallaItem.setTag(R.drawable.no_cumple_red);
                                    Intent mainIntent = new Intent().setClass(
                                            CheckListEntrega.this, DetallarProblema.class);
                                    mainIntent.putExtra("proyecto", nombre_proyecto);
                                    mainIntent.putExtra("id_proyecto", id_proyecto);
                                    mainIntent.putExtra("id_propiedad", id_propiedad);
                                    mainIntent.putExtra("id_recinto", lista_recintos.get(pos_recinto).getId_recinto());
                                    mainIntent.putExtra("id_lugar", lista_recintos.get(pos_recinto).getmLugarList().get(pos_lugar).getId_lugar());
                                    mainIntent.putExtra("nombre_item", lista_recintos.get(pos_recinto).getmLugarList().get(pos_lugar).getmItemList().get(pos_item).getItem_nombre());
                                    mainIntent.putExtra("id_item", lista_recintos.get(pos_recinto).getmLugarList().get(pos_lugar).getmItemList().get(pos_item).getId_item());

                                    //mainIntent.putExtra("pos", spinner_propieadades.getSelectedItemPosition());
                                    startActivity(mainIntent);
                                } else {
                                    mBtnFallaItem.setImageResource(R.drawable.no_cumple_des_red);
                                    mBtnFallaItem.setTag(R.drawable.no_cumple_des_red);
                                }

                            }
                        });
                        mLinearDetalleItem.addView(mListaItem);
                    }
                    mLinearDetalleLugar.addView(mListaLugar);
                }
                mContenedorListView.addView(mListaRecinto);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar CheckList", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    private void abrirDialogoRecinto() {
        //se obtiene la vista de la pantalla de ultima trx
        LayoutInflater li = LayoutInflater.from(context);
        View vista_dialogo = li.inflate(R.layout.dialogo_agregar_nuevo, null);
        // se crea un contructor del dialogo
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // setea la vista al contructor del dialogo
        alertDialogBuilder.setView(vista_dialogo);
        // setea en input donde el usuario ingresa la url
        final Spinner spinner_recintos = (Spinner) vista_dialogo
                .findViewById(R.id.spinner_recintos);
        final Spinner spinner_lugar = (Spinner) vista_dialogo
                .findViewById(R.id.spinner_lugar);
        final Spinner spinner_item = (Spinner) vista_dialogo
                .findViewById(R.id.spinner_item);
        // Elementos del Spinner
        List<Recinto> recintos = mDBHelper.listarTodosRecintos(id_tipo_prop);
        // Creando adapter para el spinner
        dataAdapterRecintos = new SpinnerAdapterRecintos(context,
                android.R.layout.simple_spinner_item, recintos);
        // Estilo del Sipnner - Lista de Datos
        dataAdapterRecintos
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // enlazando la informacion al spinner
        spinner_recintos.setAdapter(dataAdapterRecintos);
        spinner_recintos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                recinto_selecionado_combo = dataAdapterRecintos.getItem(position);
                if (recinto_selecionado_combo.getId_recinto() != 0) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    progressDialog.setMessage("Cargando Data...");
                    progressDialog.show();
                    spinner_item.setAdapter(null);
                    //Se cargan los Lugares
                    List<Lugar> lugar = mDBHelper.listarLugaresRecintos(id_tipo_prop, recinto_selecionado_combo.getId_recinto());
                    dataAdapterLugares = new SpinnerAdapterLugares(context, android.R.layout.simple_spinner_item, lugar);
                    dataAdapterLugares.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_lugar.setAdapter(dataAdapterLugares);
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        spinner_lugar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                lugar_selecionado_combo = dataAdapterLugares.getItem(position);
                Log.w(TAG, "lugar_id --> " + lugar_selecionado_combo.getId_lugar());
                if (lugar_selecionado_combo.getId_lugar() != 0) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    progressDialog.setMessage("Cargando Data...");
                    progressDialog.show();
                    //spinner_item.setAdapter(null);
                    //Se cargan los Lugares
                    List<Item> item = mDBHelper.listarItemLugaresRecintos(id_tipo_prop, recinto_selecionado_combo.getId_recinto(), lugar_selecionado_combo.getId_lugar());
                    dataAdapterItem = new SpinnerAdapterItem(context, android.R.layout.simple_spinner_item, item);
                    dataAdapterItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_item.setAdapter(dataAdapterItem);
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        spinner_item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (dataAdapterItem != null)
                    item_selecionado_combo = dataAdapterItem.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        // Configura el mensaje de dialogo
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setTitle("Ingresar Datos");
        // Se cera el dialogo
        AlertDialog alertDialog = alertDialogBuilder.create();
        // Se muestra el dialogo
        alertDialog.show();
        Button theButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        theButton.setOnClickListener(new AgregarRecintoListener(alertDialog));
    }

    class AgregarRecintoListener implements View.OnClickListener {
        private final Dialog dialog;

        public AgregarRecintoListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here

            if (recinto_selecionado_combo == null) {
                Toast.makeText(context, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
            } else if (lugar_selecionado_combo == null) {
                Toast.makeText(context, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
            } else if (item_selecionado_combo == null) {
                Toast.makeText(context, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
            } else if (recinto_selecionado_combo.getId_recinto() == 0) {
                Toast.makeText(context, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
            } else if (lugar_selecionado_combo.getId_lugar() == 0) {
                Toast.makeText(context, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
            } else if (item_selecionado_combo.getId_item() == 0) {
                Toast.makeText(context, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
            } else {
                //Se crea un array para poblar los Lugares
                ArrayList<Lugar> lista_lugar = new ArrayList<Lugar>();
                //Se crea un array para poblar los Items
                ArrayList<Item> lista_item = new ArrayList<Item>();
                lista_item.add(item_selecionado_combo);
                lista_lugar.add(new Lugar(lugar_selecionado_combo.getId_lugar(), lugar_selecionado_combo.getLugar_nombre(), lista_item));
                int pos_recinto = recinto_existente(recinto_selecionado_combo.getId_recinto());
                if (pos_recinto == -1)
                    lista_recintos.add(new Recinto(recinto_selecionado_combo.getId_recinto(), recinto_selecionado_combo.getRecinto_nombre(), lista_lugar));
                else
                    lista_recintos.get(pos_recinto).getmLugarList().add(new Lugar(lugar_selecionado_combo.getId_lugar(), lugar_selecionado_combo.getLugar_nombre(), lista_item));
                dialog.dismiss();
                recargarVistaCheckList();
            }
        }
    }

    private int recinto_existente(int id_recinto) {
        for (int i = 0; i < lista_recintos.size(); i++) {
            if (lista_recintos.get(i).getId_recinto() == id_recinto) {
                Log.d(TAG, "ID RECINTO : (" + i + ")" + id_recinto);
                return i;
            }
        }
        return -1;

    }

    private void abrirDialogoLugar(final int id_recinto, String nombre_recinto, int pos_recinto) {
        pos_recinto_agregar = pos_recinto;
        //se obtiene la vista de la pantalla de ultima trx
        LayoutInflater li = LayoutInflater.from(context);
        View vista_dialogo = li.inflate(R.layout.dialogo_agregar_lugar, null);
        // se crea un contructor del dialogo
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // setea la vista al contructor del dialogo
        alertDialogBuilder.setView(vista_dialogo);
        // setea en input donde el usuario ingresa la url
        final TextView spinner_recintos = (TextView) vista_dialogo
                .findViewById(R.id.spinner_recintos);
        spinner_recintos.setText(nombre_recinto);
        final Spinner spinner_lugar = (Spinner) vista_dialogo
                .findViewById(R.id.spinner_lugar);
        final Spinner spinner_item = (Spinner) vista_dialogo
                .findViewById(R.id.spinner_item);
        // Elementos del Spinner
        List<Lugar> lugar = mDBHelper.listarLugaresRecintos(id_tipo_prop, id_recinto);
        dataAdapterLugares = new SpinnerAdapterLugares(context, android.R.layout.simple_spinner_item, lugar);
        dataAdapterLugares.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lugar.setAdapter(dataAdapterLugares);
        spinner_lugar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                lugar_selecionado_combo = dataAdapterLugares.getItem(position);
                Log.w(TAG, "lugar_id --> " + lugar_selecionado_combo.getId_lugar());
                if (lugar_selecionado_combo.getId_lugar() != 0) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    progressDialog.setMessage("Cargando Data...");
                    progressDialog.show();
                    //spinner_item.setAdapter(null);
                    //Se cargan los Lugares
                    List<Item> item = mDBHelper.listarItemLugaresRecintos(id_tipo_prop, id_recinto, lugar_selecionado_combo.getId_lugar());
                    dataAdapterItem = new SpinnerAdapterItem(context, android.R.layout.simple_spinner_item, item);
                    dataAdapterItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_item.setAdapter(dataAdapterItem);
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        spinner_item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (dataAdapterItem != null)
                    item_selecionado_combo = dataAdapterItem.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        // Configura el mensaje de dialogo
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setTitle("Ingresar Datos");
        // Se cera el dialogo
        AlertDialog alertDialog = alertDialogBuilder.create();
        // Se muestra el dialogo
        alertDialog.show();
        Button theButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        theButton.setOnClickListener(new AgregarLugarListener(alertDialog));
    }

    class AgregarLugarListener implements View.OnClickListener {
        private final Dialog dialog;

        public AgregarLugarListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here

            if (lugar_selecionado_combo == null) {
                Toast.makeText(context, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
            } else if (item_selecionado_combo == null) {
                Toast.makeText(context, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
            } else if (lugar_selecionado_combo.getId_lugar() == 0) {
                Toast.makeText(context, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
            } else if (item_selecionado_combo.getId_item() == 0) {
                Toast.makeText(context, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
            } else {
                //Se crea un array para poblar los Lugares
                //ArrayList<Lugar> lista_lugar = lista_recintos.get(pos_recinto_agregar).getmLugarList().;
                //Se crea un array para poblar los Items
                ArrayList<Item> lista_item = new ArrayList<Item>();
                lista_item.add(item_selecionado_combo);
                lista_recintos.get(pos_recinto_agregar).getmLugarList().add(new Lugar(lugar_selecionado_combo.getId_lugar(), lugar_selecionado_combo.getLugar_nombre(), lista_item));
                //lista_lugar.add(new Lugar(lugar_selecionado_combo.getId_lugar(), lugar_selecionado_combo.getLugar_nombre(), lista_item));
                //lista_recintos.add(new Recinto(recinto_selecionado_combo.getId_recinto(), recinto_selecionado_combo.getRecinto_nombre(), lista_lugar));
                dialog.dismiss();
                recargarVistaCheckList();
            }
        }
    }

    private void abrirDialogoItem(final int id_recinto, String nombre_recinto, int pos_recinto, final int id_lugar, String nombre_lugar, int pos_lugar) {
        pos_recinto_agregar = pos_recinto;
        pos_lugar_agregar = pos_lugar;
        //se obtiene la vista de la pantalla de ultima trx
        LayoutInflater li = LayoutInflater.from(context);
        View vista_dialogo = li.inflate(R.layout.dialogo_agregar_item, null);
        // se crea un contructor del dialogo
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // setea la vista al contructor del dialogo
        alertDialogBuilder.setView(vista_dialogo);
        // setea en input donde el usuario ingresa la url
        final TextView spinner_recintos = (TextView) vista_dialogo
                .findViewById(R.id.spinner_recintos);
        spinner_recintos.setText(nombre_recinto);
        final TextView spinner_lugar = (TextView) vista_dialogo
                .findViewById(R.id.spinner_lugar);
        spinner_lugar.setText(nombre_lugar);
        final Spinner spinner_item = (Spinner) vista_dialogo
                .findViewById(R.id.spinner_item);
        // Elementos del Spinner
        List<Item> item = mDBHelper.listarItemLugaresRecintos(id_tipo_prop, id_recinto, id_lugar);
        dataAdapterItem = new SpinnerAdapterItem(context, android.R.layout.simple_spinner_item, item);
        dataAdapterItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_item.setAdapter(dataAdapterItem);
        spinner_item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (dataAdapterItem != null)
                    item_selecionado_combo = dataAdapterItem.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        // Configura el mensaje de dialogo
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setTitle("Ingresar Datos");
        // Se cera el dialogo
        AlertDialog alertDialog = alertDialogBuilder.create();
        // Se muestra el dialogo
        alertDialog.show();
        Button theButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        theButton.setOnClickListener(new AgregarItemListener(alertDialog));
    }

    class AgregarItemListener implements View.OnClickListener {
        private final Dialog dialog;

        public AgregarItemListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here
            if (item_selecionado_combo == null) {
                Toast.makeText(context, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
            } else if (item_selecionado_combo.getId_item() == 0) {
                Toast.makeText(context, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
            } else {
                //Se crea un array para poblar los Lugares
                //ArrayList<Lugar> lista_lugar = lista_recintos.get(pos_recinto_agregar).getmLugarList().;
                //Se crea un array para poblar los Items
                ArrayList<Item> lista_item = new ArrayList<Item>();
                lista_item.add(item_selecionado_combo);
                lista_recintos.get(pos_recinto_agregar).getmLugarList().get(pos_lugar_agregar).getmItemList().add(item_selecionado_combo);
                //add(new Lugar(lugar_selecionado_combo.getId_lugar(), lugar_selecionado_combo.getLugar_nombre(), lista_item));
                //lista_lugar.add(new Lugar(lugar_selecionado_combo.getId_lugar(), lugar_selecionado_combo.getLugar_nombre(), lista_item));
                //lista_recintos.add(new Recinto(recinto_selecionado_combo.getId_recinto(), recinto_selecionado_combo.getRecinto_nombre(), lista_lugar));
                dialog.dismiss();
                recargarVistaCheckList();
            }
        }
    }
}
