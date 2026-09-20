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
 * Gestiona la búsqueda de sesión por código (RF-11, RF-12),
 * validación de duplicados (RF-15 / ERR-05) y registro de asistencia en Firebase (RF-13).
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
     * Valida la sesión y registra al alumno en sesiones/{idSesion}/alumnos/{idAlumno} (RF-13).
     * Controla ERR-01 (código no existe), ERR-02 (sesión cerrada) y ERR-05 (alumno duplicado).
     */
    public void registrarAsistencia(String codigo, String idAlumno, String nombre, RegistroCallback callback) {
        sesionesRef.orderByChild("codigo").equalTo(codigo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // ERR-01: Código no existe
                if (!snapshot.exists() || snapshot.getChildrenCount() == 0) {
                    callback.onError("Sesión no encontrada con el código ingresado.");
                    return;
                }

                DataSnapshot sesionSnapshot = snapshot.getChildren().iterator().next();
                Sesion sesion = sesionSnapshot.getValue(Sesion.class);
                String idSesion = sesionSnapshot.getKey();

                if (sesion == null || idSesion == null) {
                    callback.onError("Error al obtener los datos de la sesión.");
                    return;
                }

                // ERR-02: Sesión cerrada (activa == false)
                if (!sesion.isActiva()) {
                    callback.onError("La sesión está cerrada. No se aceptan nuevos registros.");
                    return;
                }

                // RF-15 / ERR-05: Validar si el alumno ya está registrado en esta sesión
                String idLimpio = idAlumno.trim();
                if (sesionSnapshot.child("alumnos").hasChild(idLimpio)) {
                    callback.onError("Ya registraste asistencia en esta sesión.");
                    return;
                }

                // RF-13: Registrar bajo alumnos/{idAlumno}
                String horaRegistro = DateTimeFormatter.ISO_INSTANT.format(Instant.now());

                Map<String, Object> datosAlumno = new HashMap<>();
                datosAlumno.put("nombre", nombre.trim());
                datosAlumno.put("horaRegistro", horaRegistro);

                sesionesRef.child(idSesion)
                        .child("alumnos")
                        .child(idLimpio)
                        .setValue(datosAlumno)
                        .addOnSuccessListener(aVoid -> {
                            String curso = sesion.getCurso() != null ? sesion.getCurso() : "Curso";
                            callback.onSuccess(curso, horaRegistro);
                        })
                        .addOnFailureListener(e -> {
                            callback.onError("Error al registrar en Firebase: " + e.getMessage());
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // ERR-04: RTDB sin conexión
                callback.onError("Sin conexión con el servidor. Intenta de nuevo.");
            }
        });
    }
}