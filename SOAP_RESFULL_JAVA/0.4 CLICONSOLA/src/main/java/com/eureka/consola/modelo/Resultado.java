package com.eureka.consola.modelo;

import java.util.List;

public class Resultado {
    private boolean exito;
    private String mensaje;

    public Resultado(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public boolean isExito() { return exito; }
    public String getMensaje() { return mensaje; }
}