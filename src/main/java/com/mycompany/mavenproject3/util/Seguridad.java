package com.mycompany.mavenproject3.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

/** Utilidades de seguridad: hash de contrasenas y token CSRF. */
public final class Seguridad {

    private static final SecureRandom RANDOM = new SecureRandom();

    private Seguridad() { }

    /** SHA-256 en hexadecimal (64 caracteres). */
    public static String sha256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(texto.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /** Compara una contrasena en texto plano contra el hash guardado en Empleado.ContrasenaHash. */
    public static boolean verificar(String plano, String hashGuardado) {
        if (plano == null || hashGuardado == null) {
            return false;
        }
        return MessageDigest.isEqual(
                sha256(plano).toLowerCase().getBytes(StandardCharsets.UTF_8),
                hashGuardado.trim().toLowerCase().getBytes(StandardCharsets.UTF_8));
    }

    public static String tokenAleatorio() {
        byte[] b = new byte[24];
        RANDOM.nextBytes(b);
        return HexFormat.of().formatHex(b);
    }
}
