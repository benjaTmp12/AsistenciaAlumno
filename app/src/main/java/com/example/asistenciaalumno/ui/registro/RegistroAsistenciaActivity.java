package com.example.asistenciaalumno.ui.registro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.asistenciaalumno.databinding.ActivityRegistroAsistenciaBinding;
import com.example.asistenciaalumno.ui.confirmacion.ConfirmacionAsistenciaActivity;

/**
 * P-AL01 — Pantalla de Registro de Asistencia del Alumno.
 * Permite ingresar el código de 4 dígitos (RF-11), el ID/RUT y el nombre (RF-10).
 */
public class RegistroAsistenciaActivity extends AppCompatActivity {

    private ActivityRegistroAsistenciaBinding binding;
    private RegistroAsistenciaViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroAsistenciaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(RegistroAsistenciaViewModel.class);

        configurarEventos();
        observarViewModel();
    }

    private void configurarEventos() {
        binding.btnRegistrar.setOnClickListener(v -> {
            String codigo = binding.etCodigo.getText() != null ? binding.etCodigo.getText().toString() : "";
            String idAlumno = binding.etIdAlumno.getText() != null ? binding.etIdAlumno.getText().toString() : "";
            String nombre = binding.etNombre.getText() != null ? binding.etNombre.getText().toString() : "";

            viewModel.registrarAsistencia(codigo, idAlumno, nombre);
        });
    }

    private void observarViewModel() {
        // Estado de carga
        viewModel.getCargando().observe(this, cargando -> {
            boolean estaCargando = Boolean.TRUE.equals(cargando);
            binding.progressBarRegistro.setVisibility(estaCargando ? View.VISIBLE : View.GONE);
            binding.btnRegistrar.setEnabled(!estaCargando);
            binding.etCodigo.setEnabled(!estaCargando);
            binding.etIdAlumno.setEnabled(!estaCargando);
            binding.etNombre.setEnabled(!estaCargando);
        });

        // Validaciones de campos
        viewModel.getErrorCodigo().observe(this, error -> binding.tilCodigo.setError(error));
        viewModel.getErrorIdAlumno().observe(this, error -> binding.tilIdAlumno.setError(error));
        viewModel.getErrorNombre().observe(this, error -> binding.tilNombre.setError(error));

        // Error general / Firebase (sesión no existe o cerrada)
        viewModel.getErrorGeneral().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                binding.tvErrorGeneral.setText(error);
                binding.tvErrorGeneral.setVisibility(View.VISIBLE);
            } else {
                binding.tvErrorGeneral.setVisibility(View.GONE);
            }
        });

        // Registro exitoso -> Pantalla de confirmación (P-AL02 / RF-14)
        viewModel.getRegistroExitoso().observe(this, resultado -> {
            if (resultado != null) {
                Intent intent = new Intent(RegistroAsistenciaActivity.this, ConfirmacionAsistenciaActivity.class);
                intent.putExtra(ConfirmacionAsistenciaActivity.EXTRA_CURSO, resultado.curso);
                intent.putExtra(ConfirmacionAsistenciaActivity.EXTRA_ID_ALUMNO, resultado.idAlumno);
                intent.putExtra(ConfirmacionAsistenciaActivity.EXTRA_NOMBRE, resultado.nombre);
                intent.putExtra(ConfirmacionAsistenciaActivity.EXTRA_HORA, resultado.horaRegistro);
                startActivity(intent);
                finish();
            }
        });
    }
}
