package com.example.tasksave.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.tasksave.R;

public class activity_confg_aparencia extends AppCompatActivity {

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent2 = new Intent(activity_confg_aparencia.this, activity_config.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent2);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private LinearLayout linearLayoutTema;
    private String modoSelecionado, textViewString;
    private TextView textViewModoTema;
    private ImageView imageViewBack;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confg_aparencia);

        linearLayoutTema = findViewById(R.id.linearLayout2);
        textViewModoTema = findViewById(R.id.textViewModo);
        imageViewBack = findViewById(R.id.imageView4);

        textViewModoTema.setText(carregaTema());

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(activity_confg_aparencia.this, activity_config.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        linearLayoutTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewString = textViewModoTema.getText().toString();
                dialogRepeater(textViewString);
            }
        });

    }
    void dialogRepeater(String textoRepeater) {

        Dialog dialog2 = new Dialog(activity_confg_aparencia.this, R.style.DialogTheme2);
        dialog2.setContentView(R.layout.dialog_theme); // Defina o layout do diálogo
        dialog2.setCancelable(true); // Permita que o usuário toque fora do diálogo para fechá-lo
        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        Window window = dialog2.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        RadioGroup radioGroup = dialog2.findViewById(R.id.RadioGroup);
        RadioButton radioButtonDefault = dialog2.findViewById(R.id.radioAuto);
        RadioButton radioButtonEscuro = dialog2.findViewById(R.id.radioEscuro);
        RadioButton radioButtonClaro = dialog2.findViewById(R.id.radioClaro);

        if (textoRepeater != null) {

            switch (textoRepeater) {

                case "Automático":
                    radioButtonDefault.setChecked(true);
                    break;
                case "Escuro":
                    radioButtonEscuro.setChecked(true);
                    break;
                case "Claro":
                    radioButtonClaro.setChecked(true);
                    break;
            }
        } else {
            radioButtonDefault.setChecked(true);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Obter a string do RadioButton selecionado
                RadioButton radioButton = dialog2.findViewById(checkedId);
                modoSelecionado = radioButton.getText().toString();
                textViewModoTema.setText(modoSelecionado);

                if(modoSelecionado.equals("Automático")) {

                    SharedPreferences prefs0 = getSharedPreferences("ArquivoTema", MODE_PRIVATE);
                    SharedPreferences.Editor editor0 = prefs0.edit();
                    editor0.putString("ArqTemaString", "Automático");
                    editor0.commit();

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

                }else if(modoSelecionado.equals("Escuro")) {

                    SharedPreferences prefs0 = getSharedPreferences("ArquivoTema", MODE_PRIVATE);
                    SharedPreferences.Editor editor0 = prefs0.edit();
                    editor0.putString("ArqTemaString", "Escuro");
                    editor0.commit();

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                }else if(modoSelecionado.equals("Claro")) {

                    SharedPreferences prefs0 = getSharedPreferences("ArquivoTema", MODE_PRIVATE);
                    SharedPreferences.Editor editor0 = prefs0.edit();
                    editor0.putString("ArqTemaString", "Claro");
                    editor0.commit();


                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                // Fechar o diálogo
                dialog2.dismiss();
            }
        });
        dialog2.show();
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }
    public String carregaTema() {

        SharedPreferences sharedPreferences = getSharedPreferences("ArquivoTema", MODE_PRIVATE);
        String temaCarregado = sharedPreferences.getString("ArqTemaString", "Automático");

        return temaCarregado;

    }
}