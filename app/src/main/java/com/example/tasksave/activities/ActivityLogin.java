package com.example.tasksave.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.tasksave.R;
import com.example.tasksave.dao.UsuarioDAOMYsql;
import com.example.tasksave.objetos.User;
import com.example.tasksave.sharedPreferences.SharedPreferencesUsuario;
import com.google.android.material.snackbar.Snackbar;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActivityLogin extends AppCompatActivity {
    public EditText input_Nome;
    public EditText input_Password;
    CheckBox checkBox;
    private Button button_cadastro;
    private FrameLayout frameLayout;
    private TextView textView;
    private ProgressBar progressBar;

    String str, str2;
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

    }

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        input_Nome = findViewById(R.id.editTextEmail);
        input_Password = findViewById(R.id.editTextSenha);
        button_cadastro = findViewById(R.id.buttonLogin2);
//        input_Nome.requestFocus();
        checkBox = findViewById(R.id.checkBox2);
        frameLayout = findViewById(R.id.framelayout1);
        textView = findViewById(R.id.textviewbutton);
        progressBar = findViewById(R.id.progressbar1);

        WindowAdjuster.assistActivity(this);

        InputFilter noSpaceFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };

        input_Nome.setFilters(new InputFilter[]{noSpaceFilter});
        input_Password.setFilters(new InputFilter[]{noSpaceFilter});

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (input_Nome.getText().toString().equals("") || input_Password.getText().toString().equals("")) {

                    String msg_error2 = "Os campos não podem ser vazios.";
                    Snackbar snackbar = Snackbar.make(view, msg_error2, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                } else {
                    textView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    frameLayout.setClickable(false);
                    button_cadastro.setClickable(false);
                    escondeTeclado();
                    Autentica(ActivityLogin.this);

                }

            }

        });

        button_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivityCadastro.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

    public void Autentica(Context context) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                String emailUser = input_Nome.getText().toString();
                String senhaUser = input_Password.getText().toString();

                User user = new User();
                user.setEmail_usuario(emailUser);
                user.setSenha_usuario(senhaUser);

                UsuarioDAOMYsql usuarioDAOMYsql = new UsuarioDAOMYsql();
                ResultSet resultSet = usuarioDAOMYsql.autenticaUsuarioAWS(user);


                String userShared = usuarioDAOMYsql.usuarioCadastrado(emailUser, senhaUser);
                int idcargoUsuario = usuarioDAOMYsql.cargoUsuarioAWS(emailUser);


                SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(context);

                if (resultSet.next()) {
                    // Sucesso na autenticação
                    str = "Sucesso";
                    runOnUiThread(() -> {

                        if(checkBox.isChecked()) {

                            sharedPreferencesUsuario.armazenaSalvarSenhaBL(true);

                            sharedPreferencesUsuario.armazenaPrimeiroAcesso(true);

                            sharedPreferencesUsuario.armazenaEmailLogin(emailUser);

                            sharedPreferencesUsuario.armazenaSenhaLogin(senhaUser);

                            sharedPreferencesUsuario.armazenaUsuarioLogin(userShared);

                        }else {

                            sharedPreferencesUsuario.armazenaSalvarSenhaBL(false);

                            sharedPreferencesUsuario.armazenaUsuarioLogin(userShared);


                        }

                        sharedPreferencesUsuario.armazenaUsuarioCargo(idcargoUsuario);

                        sharedPreferencesUsuario.armazenaPrimeiroAcesso(true);

                        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    });
                } else {
                    // Falha na autenticação
                    str2 = "Usuário ou senha incorreta.";
                    runOnUiThread(() -> {
                        textView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        frameLayout.setClickable(true);
                        button_cadastro.setClickable(true);
                        Snackbar snackbar = Snackbar.make(this.getCurrentFocus(), str2, Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    });
                }
            } catch (SQLException e) {
                Log.d("ERRO SQL AUT", "ERRO SQL" + e);
            }
        });
    }
    public void escondeTeclado() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            View currentFocus = getCurrentFocus();
            if (currentFocus != null) {
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
    }
}






