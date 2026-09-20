package com.example.asistenciaalumno.ui.confirmacion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asistenciaalumno.databinding.ActivityConfirmacionAsistenciaBinding;

/**
 * P-AL02 — Pantalla de Confirmación de Asistencia del Alumno (RF-14).
 * Muestra el resumen visual con check verde, curso, alumno y hora.
 */
public class ConfirmacionAsistenciaActivity extends AppCompatActivity {

    public static final String EXTRA_CURSO = "extra_curso";
    public static final String EXTRA_ID_ALUMNO = "extra_id_alumno";
    public static final String EXTRA_NOMBRE = "extra_nombre";
    public static final String EXTRA_HORA = "extra_hora";

    private ActivityConfirmacionAsistenciaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmacionAsistenciaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String curso = getIntent().getStringExtra(EXTRA_CURSO);
        String idAlumno = getIntent().getStringExtra(EXTRA_ID_ALUMNO);
        String nombre = getIntent().getStringExtra(EXTRA_NOMBRE);
        String hora = getIntent().getStringExtra(EXTRA_HORA);

        mostrarDetalles(curso, idAlumno, nombre, hora);

        binding.btnAceptar.setOnClickListener(v -> finish());
    }

    private void mostrarDetalles(String curso, String idAlumno, String nombre, String hora) {
        if (curso != null) {
            binding.tvCursoConfirmado.setText(curso);
        }

        String textoAlumno = (nombre != null ? nombre : "") + (idAlumno != null ? " (" + idAlumno + ")" : "");
        binding.tvAlumnoConfirmado.setText(textoAlumno);

        if (hora != null && hora.contains("T") && hora.length() >= 19) {
            // Formatear ISO 8601 a HH:mm:ss
            String horaFormateada = hora.substring(hora.indexOf("T") + 1, hora.indexOf("T") + 9);
            binding.tvHoraConfirmada.setText(horaFormateada + " hrs");
        } else if (hora != null) {
            binding.tvHoraConfirmada.setText(hora);
        }
    }
}
