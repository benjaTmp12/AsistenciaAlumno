package com.example.asistenciaalumno.ui.registro;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.asistenciaalumno.data.repository.AlumnoRepository;

/**
 * ViewModel para la pantalla de Registro del Alumno (P-AL01).
 * Implementa las validaciones de RF-10 y RF-11 y coordina el registro con Firebase.
 */
public class RegistroAsistenciaViewModel extends ViewModel {

    public static class ResultadoRegistro {
        public final String curso;
        public final String idAlumno;
        public final String nombre;
        public final String horaRegistro;

        public ResultadoRegistro(String curso, String idAlumno, String nombre, String horaRegistro) {
            this.curso = curso;
            this.idAlumno = idAlumno;
            this.nombre = nombre;
            this.horaRegistro = horaRegistro;
        }
    }

    private final AlumnoRepository repository;

    private final MutableLiveData<Boolean> cargando = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorCodigo = new MutableLiveData<>(null);
    private final MutableLiveData<String> errorIdAlumno = new MutableLiveData<>(null);
    private final MutableLiveData<String> errorNombre = new MutableLiveData<>(null);
    private final MutableLiveData<String> errorGeneral = new MutableLiveData<>(null);
    private final MutableLiveData<ResultadoRegistro> registroExitoso = new MutableLiveData<>(null);

    public RegistroAsistenciaViewModel() {
        this.repository = new AlumnoRepository();
    }

    public RegistroAsistenciaViewModel(AlumnoRepository repository) {
        this.repository = repository;
    }

    public LiveData<Boolean> getCargando() {
        return cargando;
    }

    public LiveData<String> getErrorCodigo() {
        return errorCodigo;
    }

    public LiveData<String> getErrorIdAlumno() {
        return errorIdAlumno;
    }

    public LiveData<String> getErrorNombre() {
        return errorNombre;
    }

    public LiveData<String> getErrorGeneral() {
        return errorGeneral;
    }

    public LiveData<ResultadoRegistro> getRegistroExitoso() {
        return registroExitoso;
    }

    /**
     * Valida los campos ingresados y solicita el registro de asistencia en Firebase.
     */
    public void registrarAsistencia(String codigo, String idAlumno, String nombre) {
        // Limpiar errores previos
        errorCodigo.setValue(null);
        errorIdAlumno.setValue(null);
        errorNombre.setValue(null);
        errorGeneral.setValue(null);

        boolean esValido = true;

        // Validación RF-11: Código numérico de exactamente 4 dígitos
        if (codigo == null || !codigo.trim().matches("^\\d{4}$")) {
            errorCodigo.setValue("El código debe ser de exactamente 4 dígitos numéricos.");
            esValido = false;
        }

        // Validación RF-10: idAlumno entre 5 y 20 caracteres
        if (idAlumno == null || idAlumno.trim().length() < 5 || idAlumno.trim().length() > 20) {
            errorIdAlumno.setValue("El ID / RUT debe tener entre 5 y 20 caracteres.");
            esValido = false;
        }

        // Validación Nombre: Mínimo 3 caracteres
        if (nombre == null || nombre.trim().length() < 3) {
            errorNombre.setValue("Por favor ingresa tu nombre completo (mínimo 3 letras).");
            esValido = false;
        }

        if (!esValido) {
            return;
        }

        cargando.setValue(true);

        repository.registrarAsistencia(codigo.trim(), idAlumno.trim(), nombre.trim(), new AlumnoRepository.RegistroCallback() {
            @Override
            public void onSuccess(String curso, String horaRegistro) {
                cargando.setValue(false);
                registroExitoso.setValue(new ResultadoRegistro(curso, idAlumno.trim(), nombre.trim(), horaRegistro));
            }

            @Override
            public void onError(String mensajeError) {
                cargando.setValue(false);
                errorGeneral.setValue(mensajeError);
            }
        });
    }
}
