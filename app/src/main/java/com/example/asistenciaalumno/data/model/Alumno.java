package com.example.asistenciaalumno.data.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Alumno {

    private String idAlumno;
    private String nombre;
    private String horaRegistro;

    public Alumno() {
    }

    public Alumno(String nombre, String horaRegistro) {
        this.nombre = nombre;
        this.horaRegistro = horaRegistro;
    }

    public Alumno(String idAlumno, String nombre, String horaRegistro) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.horaRegistro = horaRegistro;
    }

    @Exclude
    public String getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHoraRegistro() {
        return horaRegistro;
    }

    public void setHoraRegistro(String horaRegistro) {
        this.horaRegistro = horaRegistro;
    }
}
