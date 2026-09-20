package com.example.asistenciaalumno.data.repository;

import androidx.annotation.NonNull;

import com.example.asistenciaalumno.data.model.Alumno;
import com.example.asistenciaalumno.data.model.Sesion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Repositorio para la App Alumno.
 * Gestiona la búsqueda de sesión por código (RF-11, RF-12)
 * y el registro de asistencia en Firebase Realtime Database (RF-13).
 */
public class AlumnoRepository {

    private final DatabaseReference sesionesRef;

    public interface RegistroCallback {
        void onSuccess(String curso, String horaRegistro);
        void onError(String mensajeError);
    }

    public AlumnoRepository() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.sesionesRef = db.getReference("sesiones");
    }

    public AlumnoRepository(DatabaseReference sesionesRef) {
        this.sesionesRef = sesionesRef;
    }

    /**
     * Valida la existencia y estado activo de la sesión por código (RF-11, RF-12)
     * y registra al alumno en sesiones/{idSesion}/alumnos/{idAlumno} (RF-13).
     */
    public void registrarAsistencia(String codigo, String idAlumno, String nombre, RegistroCallback callback) {
        sesionesRef.orderByChild("codigo").equalTo(codigo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists() || snapshot.getChildrenCount() == 0) {
                    callback.onError("Código inválido: no se encontró ninguna sesión con ese código.");
                    return;
                }

                // Obtener la sesión coincidente
                DataSnapshot sesionSnapshot = snapshot.getChildren().iterator().next();
                Sesion sesion = sesionSnapshot.getValue(Sesion.class);
                String idSesion = sesionSnapshot.getKey();

                if (sesion == null || idSesion == null) {
                    callback.onError("Error al obtener los datos de la sesión.");
                    return;
                }

                // RF-12: Validar que la sesión esté activa
                if (!sesion.isActiva()) {
                    callback.onError("Esta sesión ya ha sido finalizada por el docente. No se aceptan nuevos registros.");
                    return;
                }

                // RF-13: Registrar en sesiones/{idSesion}/alumnos/{idAlumno}
                String horaRegistro = DateTimeFormatter.ISO_INSTANT.format(Instant.now());

                Map<String, Object> datosAlumno = new HashMap<>();
                datosAlumno.put("nombre", nombre.trim());
                datosAlumno.put("horaRegistro", horaRegistro);

                sesionesRef.child(idSesion)
                        .child("alumnos")
                        .child(idAlumno.trim())
                        .setValue(datosAlumno)
                        .addOnSuccessListener(aVoid -> {
                            String curso = sesion.getCurso() != null ? sesion.getCurso() : "Curso";
                            callback.onSuccess(curso, horaRegistro);
                        })
                        .addOnFailureListener(e -> {
                            callback.onError("Error al registrar asistencia en Firebase: " + e.getMessage());
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError("Error de conexión con Firebase: " + error.getMessage());
            }
        });
    }
}
