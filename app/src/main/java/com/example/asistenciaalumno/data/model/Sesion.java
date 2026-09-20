package com.example.asistenciaalumno.data.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Sesion {

    private String idSesion;
    private boolean activa;
    private String codigo;
    private String curso;
    private String creadoEn;
    private Map<String, Alumno> alumnos = new HashMap<>();

    public Sesion() {
    }

    @Exclude
    public String getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(String idSesion) {
        this.idSesion = idSesion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(String creadoEn) {
        this.creadoEn = creadoEn;
    }

    public Map<String, Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(Map<String, Alumno> alumnos) {
        this.alumnos = alumnos != null ? alumnos : new HashMap<>();
    }
}
