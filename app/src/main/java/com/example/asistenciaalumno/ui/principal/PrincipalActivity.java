package com.example.asistenciaalumno.ui.principal;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asistenciaalumno.databinding.ActivityPrincipalAlumnoBinding;
import com.example.asistenciaalumno.ui.registro.RegistroAsistenciaActivity;

/**
 * P-AL01 — Pantalla Principal del Alumno.
 * Punto de entrada con botón para ir a registrar asistencia (P-AL02).
 */
public class PrincipalActivity extends AppCompatActivity {

    private ActivityPrincipalAlumnoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrincipalAlumnoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnIrRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(PrincipalActivity.this, RegistroAsistenciaActivity.class);
            startActivity(intent);
        });
    }
}