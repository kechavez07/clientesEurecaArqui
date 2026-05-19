package com.eureka.consola.modelo;

public class Movimiento {
    private String cuenta;
    private String nromov;
    private String fecha;
    private String tipo;
    private String accion;
    private String importe;

    public Movimiento() {}

    public Movimiento(String cuenta, String nromov, String fecha, String tipo, String accion, String importe) {
        this.cuenta = cuenta;
        this.nromov = nromov;
        this.fecha = fecha;
        this.tipo = tipo;
        this.accion = accion;
        this.importe = importe;
    }

    public String getCuenta() { return cuenta; }
    public void setCuenta(String cuenta) { this.cuenta = cuenta; }
    public String getNromov() { return nromov; }
    public void setNromov(String nromov) { this.nromov = nromov; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public String getImporte() { return importe; }
    public void setImporte(String importe) { this.importe = importe; }
}