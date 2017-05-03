package planok.cl.android.pvi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginKey extends Activity {
    @BindView(R.id.btn_ingresar_key)
    Button btnIngresarApp;
    @BindView(R.id.edit_key_inmb)
    EditText editKeyInmb; private static final String[] DUMMY_CREDENTIALS = new String[]{
            "123456", "ABCDEF", "QWERTY"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_key);
        ButterKnife.bind(this);
        btnIngresarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean is_valid = false;
                for (String credential : DUMMY_CREDENTIALS) {
                    String user = credential;
                    if (user.equals(editKeyInmb.getText().toString().trim())) {
                        // Account exists, return true if the password matches.
                        is_valid = true;
                        Intent mainIntent = new Intent().setClass(
                                LoginKey.this, Login.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }
                if (!is_valid)
                    Toast.makeText(LoginKey.this, "Key invalida, contacte a PlanOK", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void VerificarKey(String s) {

    }
}
