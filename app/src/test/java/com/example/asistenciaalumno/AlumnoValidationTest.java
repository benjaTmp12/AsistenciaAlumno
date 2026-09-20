package com.example.asistenciaalumno;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AlumnoValidationTest {

    @Test
    public void codigoCuatroDigitos_esValido() {
        assertTrue("1234".matches("^\\d{4}$"));
        assertTrue("0000".matches("^\\d{4}$"));
        assertTrue("9999".matches("^\\d{4}$"));

        assertFalse("123".matches("^\\d{4}$"));
        assertFalse("12345".matches("^\\d{4}$"));
        assertFalse("12a4".matches("^\\d{4}$"));
        assertFalse("".matches("^\\d{4}$"));
    }

    @Test
    public void idAlumno_rangoCaracteres() {
        String idValido1 = "12345";       // 5 chars
        String idValido2 = "20123456K";    // 9 chars
        String idValido3 = "12345678901234567890"; // 20 chars

        assertTrue(idValido1.length() >= 5 && idValido1.length() <= 20);
        assertTrue(idValido2.length() >= 5 && idValido2.length() <= 20);
        assertTrue(idValido3.length() >= 5 && idValido3.length() <= 20);

        String idCorto = "1234";
        String idLargo = "123456789012345678901";
        assertFalse(idCorto.length() >= 5 && idCorto.length() <= 20);
        assertFalse(idLargo.length() >= 5 && idLargo.length() <= 20);
    }
}